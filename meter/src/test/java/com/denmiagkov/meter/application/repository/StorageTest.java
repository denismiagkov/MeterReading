package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.exception.AuthenticationFailedException;
import com.denmiagkov.meter.application.repository.Storage;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.ActivityType;
import com.denmiagkov.meter.domain.Reading;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StorageTest {
    Storage storage = new Storage();
    User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        storage.addUser(user);
    }

    @Test
    void addUser_TestAddUser() {
        assertAll(
                () -> assertThat(storage.getUsers().size()).isEqualTo(1),
                () -> assertThat(storage.getUsers().contains(user)).isTrue());
    }

    @Test
    void addUser_TestAddAdmin() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");

        storage.addUser(user2);

        assertAll(
                () -> assertThat(storage.getUsers().size()).isEqualTo(2),
                () -> assertThat(storage.getUsers().contains(user2)).isTrue());
    }

    @Test
    void addActivity() {
    }

    @Test
    void isExistUser_ReturnsTrue() {
        assertThat(storage.getUsers().contains(user)).isTrue();
    }

    @Test
    void isExistUser_ReturnsFalse() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");

        assertThat(storage.getUsers().contains(user2)).isFalse();
    }

    @Test
    void isExistLogin_ReturnsTrue() {
        List<String> logins = storage.getUsers().stream().map(e -> e.getLogin()).toList();

        assertThat(logins.contains("user")).isTrue();
    }

    @Test
    void isExistLogin_ReturnsFalse() {
        List<String> logins = storage.getUsers().stream().map(e -> e.getLogin()).toList();

        assertThat(logins.contains("user1")).isFalse();
    }

    @Test
    void authorizeUser_ReturnsUser() {
        User user1 = storage.authorizeUser("user", "123");

        assertThat(user1).isEqualTo(user);
    }

    @Test
    void authorizeUser_ThrowsExceptionIfPasswordIsIncorrect() {
        assertThatThrownBy(() -> storage.authorizeUser("user", "321"))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("Ошибка авторизации: пользователя с указанными логином и паролем не существует!");
    }

    @Test
    void addReading() {
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        Reading reading = new Reading(user, meterValues);

        storage.addNewReading(reading);

        assertThat(storage.getAllReadingsList()).isNotNull().hasSize(1);
    }

    @Test
    void getReading() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");
        storage.addUser(user2);
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        Reading reading = new Reading(user, meterValues);
        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues.put("HEATING", 71.16);
        meterValues.put("HOT_WATER", 19.17);
        meterValues.put("COLD_WATER", 14.00);
        Reading reading2 = new Reading(user2, meterValues2);
        storage.addNewReading(reading);
        storage.addNewReading(reading2);

        Reading testReading = storage.getLastReading(user);

        assertAll(
                () -> assertThat(testReading).isNotNull(),
                () -> assertThat(testReading.getValues())
                        .isEqualTo(reading.getValues())
                        .isNotEqualTo(reading2.getValues()));

    }

    @Test
    void getUsers() {
        User user2 = new User("Paul", "11-22-33", "user2", "321",
                "true", "123admin");
        storage.addUser(user);
        storage.addUser(user2);

        var users = storage.getUsers();

        assertThat(users).isNotNull().hasSize(2);
    }

    @Test
    @Disabled
    void getReadingList() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");
        storage.addUser(user2);
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        Reading reading = new Reading(user, meterValues);
        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues.put("HEATING", 71.16);
        meterValues.put("HOT_WATER", 19.17);
        meterValues.put("COLD_WATER", 14.00);
        Reading reading2 = new Reading(user2, meterValues2);
        storage.addNewReading(reading);
        storage.addNewReading(reading2);

        List<Reading> readings = storage.getAllReadingsList();

        assertThat(readings)
                .isNotNull()
                .hasSize(2)
                .contains(reading, reading2);

    }

    @Test
    void getActivityList() {
        storage.getUsers().clear();
        UserService userService = new UserService(storage);
        userService.registerUser(user.getName(), user.getPhone(), user.getAddress(), user.getLogin(), user.getPassword());
        userService.authorizeUser("user", "123");
        userService.recordExit(user);

        List<Activity> userActivities = storage.getActivitiesList();

        assertAll(
                () -> assertThat(userActivities)
                        .isNotNull()
                        .hasSize(3),
                () -> assertThat(userActivities.get(0).getAction())
                        .isEqualTo(ActivityType.REGISTRATION),
                () -> assertThat(userActivities.get(1).getAction())
                        .isEqualTo(ActivityType.AUTHORIZATION),
                () -> assertThat(userActivities.get(2).getAction())
                        .isEqualTo(ActivityType.EXIT));

    }

    @Test
    void getReadingsReport() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");
        storage.addUser(user2);
        Map<String, Double> meterValues1 = new HashMap<>();
        meterValues1.put("HEATING", 144.25);
        meterValues1.put("HOT_WATER", 37.1);
        meterValues1.put("COLD_WATER", 24.35);
        Reading reading1 = new Reading(user, meterValues1);
        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues2.put("HEATING", 71.16);
        meterValues2.put("HOT_WATER", 19.17);
        meterValues2.put("COLD_WATER", 14.00);
        Reading reading2 = new Reading(user2, meterValues2);
        Map<String, Double> meterValues3 = new HashMap<>();
        meterValues3.put("HEATING", 197.35);
        meterValues3.put("HOT_WATER", 27.45);
        meterValues3.put("COLD_WATER", 19.05);
        Reading reading3 = new Reading(user, meterValues3);
        storage.addNewReading(reading1);
        storage.addNewReading(reading2);
        storage.addNewReading(reading3);

        List<Reading> readings = storage.getReadingsHistory(user);

        assertThat(readings)
                .isNotNull()
                .hasSize(2)
                .containsExactly(reading1, reading3);
    }

    @Test
    void getReadingsForMonthByUser() throws NoSuchFieldException, IllegalAccessException {
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        Reading reading1 = new Reading(user, meterValues);

        Class<?> clazz = Reading.class;
        Field fieldDate = clazz.getDeclaredField("date");
        fieldDate.setAccessible(true);
        fieldDate.set(reading1, LocalDateTime.now().minusDays(50));

        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues2.put("HEATING", 197.35);
        meterValues2.put("HOT_WATER", 27.45);
        meterValues2.put("COLD_WATER", 19.05);
        Reading reading2 = new Reading(user, meterValues2);
        storage.addNewReading(reading1);
        storage.addNewReading(reading2);

        Reading testReading = storage.getReadingsForMonthByUser(user, 2023, 12);

        assertThat(testReading).isEqualTo(reading1);
    }
}