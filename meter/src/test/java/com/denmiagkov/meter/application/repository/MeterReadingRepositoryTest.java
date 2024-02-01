package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MeterReadingRepositoryTest {
    MeterReadingRepository meterReadingRepository = new MeterReadingRepository();
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        userRepository.addUser(user);
    }

    @Test
    @Disabled
    void addNewReading() {
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        MeterReading reading = new MeterReading(user, meterValues);

        meterReadingRepository.addNewReading(reading);

        assertThat(meterReadingRepository.getAllReadingsList()).isNotNull().hasSize(1);
    }

    @Test
    void getLastReading() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "aDmin", "123admin");
        userRepository.addUser(user2);
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        MeterReading reading = new MeterReading(user, meterValues);
        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues.put("HEATING", 71.16);
        meterValues.put("HOT_WATER", 19.17);
        meterValues.put("COLD_WATER", 14.00);
        MeterReading reading2 = new MeterReading(user2, meterValues2);
        meterReadingRepository.addNewReading(reading);
        meterReadingRepository.addNewReading(reading2);

        MeterReading testReading = meterReadingRepository.getLastReading(user);

        assertAll(
                () -> assertThat(testReading).isNotNull(),
                () -> assertThat(testReading.getValues())
                        .isEqualTo(reading.getValues())
                        .isNotEqualTo(reading2.getValues()));

    }


    @Test
    @Disabled
    void getAllReadingsList() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");
        userRepository.addUser(user2);
        Map<String, Double> meterValues = new HashMap<>();
        meterValues.put("HEATING", 144.25);
        meterValues.put("HOT_WATER", 37.1);
        meterValues.put("COLD_WATER", 24.35);
        MeterReading reading = new MeterReading(user, meterValues);
        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues.put("HEATING", 71.16);
        meterValues.put("HOT_WATER", 19.17);
        meterValues.put("COLD_WATER", 14.00);
        MeterReading reading2 = new MeterReading(user2, meterValues2);
        meterReadingRepository.addNewReading(reading);
        meterReadingRepository.addNewReading(reading2);

        List<MeterReading> readings = meterReadingRepository.getAllReadingsList();

        assertThat(readings)
                .isNotNull()
                .hasSize(2)
                .contains(reading, reading2);

    }

    @Test
    void getReadingsHistory() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "admin", "123admin");
        userRepository.addUser(user2);
        Map<String, Double> meterValues1 = new HashMap<>();
        meterValues1.put("HEATING", 144.25);
        meterValues1.put("HOT_WATER", 37.1);
        meterValues1.put("COLD_WATER", 24.35);
        MeterReading reading1 = new MeterReading(user, meterValues1);
        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues2.put("HEATING", 71.16);
        meterValues2.put("HOT_WATER", 19.17);
        meterValues2.put("COLD_WATER", 14.00);
        MeterReading reading2 = new MeterReading(user2, meterValues2);
        Map<String, Double> meterValues3 = new HashMap<>();
        meterValues3.put("HEATING", 197.35);
        meterValues3.put("HOT_WATER", 27.45);
        meterValues3.put("COLD_WATER", 19.05);
        MeterReading reading3 = new MeterReading(user, meterValues3);
        meterReadingRepository.addNewReading(reading1);
        meterReadingRepository.addNewReading(reading2);
        meterReadingRepository.addNewReading(reading3);

        List<MeterReading> readings = meterReadingRepository.getReadingsHistory(user);

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
        MeterReading reading1 = new MeterReading(user, meterValues);

        Class<?> clazz = MeterReading.class;
        Field fieldDate = clazz.getDeclaredField("date");
        fieldDate.setAccessible(true);
        fieldDate.set(reading1, LocalDateTime.now().minusDays(50));

        Map<String, Double> meterValues2 = new HashMap<>();
        meterValues2.put("HEATING", 197.35);
        meterValues2.put("HOT_WATER", 27.45);
        meterValues2.put("COLD_WATER", 19.05);
        MeterReading reading2 = new MeterReading(user, meterValues2);
        meterReadingRepository.addNewReading(reading1);
        meterReadingRepository.addNewReading(reading2);

        MeterReading testReading = meterReadingRepository.getReadingsForMonthByUser(user, 2023, 12);

        assertThat(testReading).isEqualTo(reading1);
    }
}