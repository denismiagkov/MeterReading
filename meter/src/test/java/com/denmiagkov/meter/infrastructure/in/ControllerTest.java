package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.MeterReading;
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
//
//    Controller controller;
//    UserService userService;
//    MeterReadingService meterReadingService;
//    UserActivityService activityService;
//    DictionaryService dictionaryService;
//
//    @BeforeEach
//    void setUp() {
//        userService = mock(UserServiceImpl.class);
//        meterReadingService = mock(MeterReadingServiceImpl.class);
//        activityService = mock(UserActivityServiceImpl.class);
//        dictionaryService = mock(DictionaryServiceImpl.class);
//        controller = new Controller(userService, meterReadingService, activityService, dictionaryService);
//    }
//
//    @Test
//    void registerUser() {
//        controller.registerUser("dummy", "dummy", "dummy", "dummy", "dummy");
//
//        verify(userService, times(1)).registerUser("dummy", "dummy",
//                "dummy", "dummy", "dummy");
//    }
//
//    @Test
//    void registerAdmin() {
//        controller.registerAdmin("dummy", "dummy", "dummy", "dummy", "dummy",
//                "dummy");
//
//        verify(userService, times(1)).registerUser("dummy", "dummy",
//                "dummy", "dummy", "dummy", "dummy");
//    }
//
//    @Test
//    void authorizeUser() {
//        User user = mock(User.class);
//        when(userService.authenticateUser(anyString(), anyString()))
//                .thenReturn(user);
//        User testUser = controller.authorizeUser("dummy", "dummy");
//
//        assertThat(testUser).isEqualTo(user);
//    }
//
//    @Test
//    void getUserList() {
//        Set<User> users = new HashSet<>();
//        when(userService.getAllUsers()).thenReturn(users);
//
//        Set<User> testUsers = controller.getAllUsers();
//
//        assertThat(testUsers).isEqualTo(users);
//    }
//
//    @Test
//    void getReadingList() {
//        List<List<MeterReading>> readings = mock(ArrayList.class);
//        when(meterReadingService.getAllReadingsList(2)).thenReturn(readings);
//
//        List<List<MeterReading>> testReadings = controller.getAllReadingsList(2);
//
//        assertThat(testReadings).isEqualTo(readings);
//    }
//
//    @Test
//    void getActivityList() {
//        List<Activity> activitiesDummy = new ArrayList<>();
//        when(activityService.getUserActivitiesList()).thenReturn(activitiesDummy);
//
//        List<Activity> activities = controller.getUserActivitiesList();
//
//        assertThat(activities).isEqualTo(activitiesDummy);
//    }
//
//    @Test
//    void addUtilityType() {
//        controller.addUtilityType("ELECTRICITY");
//
//        verify(dictionaryService, times(1)).addUtilityType("ELECTRICITY");
//    }
//
//    @Test
//    void addReading() {
//        User user = mock(User.class);
//        MeterReading reading = mock(MeterReading.class);
//
//        controller.submitNewReading(user, reading);
//
//        verify(meterReadingService, times(1)).submitNewReading(user, reading);
//    }
//
//    @Test
//    void getActualReadingByUser() {
//        User userDummy = mock(User.class);
//        MeterReading readingDummy = mock(MeterReading.class);
//        when(meterReadingService.getActualMeterReadingOnExactUtilityByUser(userDummy)).thenReturn(readingDummy);
//
//        MeterReading reading = controller.getActualMeterReadingsOnAllUtilitiesByUser(userDummy);
//
//        verify(meterReadingService, times(1)).getActualMeterReadingOnExactUtilityByUser(userDummy);
//    }
//
//    @Test
//    void getReadingsHistoryByUser() {
//        User userDummy = mock(User.class);
//        List<List<MeterReading>> readingsDummy = mock(ArrayList.class);
//        when(meterReadingService.getMeterReadingsHistoryByUser(userDummy, 1)).thenReturn(readingsDummy);
//
//        List<List<MeterReading>> readings = controller.getReadingsHistoryByUser(userDummy, 1);
//
//        assertThat(readings).isEqualTo(readingsDummy);
//    }
//
//    @Test
//    void getReadingsForMonthByUser() {
//        User userDummy = mock(User.class);
//        MeterReading readingDummy = mock(MeterReading.class);
//        when(meterReadingService.getReadingsForMonthByUser(userDummy, 2024, 1))
//                .thenReturn(readingDummy);
//
//        MeterReading reading = controller.getReadingsForMonthByUser(userDummy, 2024, 1);
//
//        assertThat(reading).isEqualTo(readingDummy);
//    }
//
//    @Test
//    void recordExit() {
//        User userDummy = mock(User.class);
//        controller.recordExit(userDummy);
//
//        verify(userService, times(1)).recordExit(userDummy);
//    }
}