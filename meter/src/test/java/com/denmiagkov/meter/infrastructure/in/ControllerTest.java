package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.service.ReadingServiceImpl;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.Reading;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ControllerTest {

    Controller controller;
    UserServiceImpl userService;
    ReadingServiceImpl readingService;

    @BeforeEach
    void setUp() {
        userService = mock(UserServiceImpl.class);
        readingService = mock(ReadingServiceImpl.class);
        controller = new Controller(userService, readingService);
    }

    @Test
    void registerUser() {
        controller.registerUser("dummy", "dummy", "dummy", "dummy", "dummy");

        verify(userService, times(1)).registerUser("dummy", "dummy",
                "dummy", "dummy", "dummy");
    }

    @Test
    void registerAdmin() {
        controller.registerAdmin("dummy", "dummy", "dummy", "dummy", "dummy",
                "dummy");

        verify(userService, times(1)).registerUser("dummy", "dummy",
                "dummy", "dummy", "dummy", "dummy");
    }

    @Test
    void authorizeUser() {
        User user = mock(User.class);
        when(userService.authorizeUser(anyString(), anyString()))
                .thenReturn(user);
        User testUser = controller.authorizeUser("dummy", "dummy");

        assertThat(testUser).isEqualTo(user);
    }

    @Test
    void getUserList() {
        Set<User> users = new HashSet<>();
        when(userService.getAllUsers()).thenReturn(users);

        Set<User> testUsers = controller.getAllUsers();

        assertThat(testUsers).isEqualTo(users);
    }

    @Test
    void getReadingList() {
        List<Reading> readings = new ArrayList<>();
        when(readingService.getAllReadingsList()).thenReturn(readings);

        List<Reading> testReadings = controller.getAllReadingsList();

        assertThat(testReadings).isEqualTo(readings);
    }

    @Test
    void getActivityList() {
        List<Activity> activitiesDummy = new ArrayList<>();
        when(userService.getUserActivitiesList()).thenReturn(activitiesDummy);

        List<Activity> activities = controller.getUserActivitiesList();

        assertThat(activities).isEqualTo(activitiesDummy);
    }

    @Test
    void addUtilityType() {
        controller.addUtilityType("ELECTRICITY");

        verify(readingService, times(1)).addUtilityType("ELECTRICITY");
    }

    @Test
    void addReading() {
        User user = mock(User.class);
        Reading reading = mock(Reading.class);

        controller.submitNewReading(user, reading);

        verify(readingService, times(1)).submitNewReading(user, reading);
    }

    @Test
    void getActualReadingByUser() {
        User userDummy = mock(User.class);
        Reading readingDummy = mock(Reading.class);
        when(readingService.getActualReadingByUser(userDummy)).thenReturn(readingDummy);

        Reading reading = controller.getActualReadingByUser(userDummy);

        verify(readingService, times(1)).getActualReadingByUser(userDummy);
    }

    @Test
    void getReadingsHistoryByUser() {
        User userDummy = mock(User.class);
        List<Reading> readingsDummy = new ArrayList<>();
        when(readingService.getReadingsHistoryByUser(userDummy)).thenReturn(readingsDummy);

        List<Reading> readings = controller.getReadingsHistoryByUser(userDummy);

        assertThat(readings).isEqualTo(readingsDummy);
    }

    @Test
    void getReadingsForMonthByUser() {
        User userDummy = mock(User.class);
        Reading readingDummy = mock(Reading.class);
        when(readingService.getReadingsForMonthByUser(userDummy, 2024, 1))
                .thenReturn(readingDummy);

        Reading reading = controller.getReadingsForMonthByUser(userDummy, 2024, 1);

        assertThat(reading).isEqualTo(readingDummy);
    }

    @Test
    void recordExit() {
        User userDummy = mock(User.class);
        controller.recordExit(userDummy);

        verify(userService, times(1)).recordExit(userDummy);
    }
}