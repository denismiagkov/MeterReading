package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.dto.MeterReadingSubmitDto;
import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
    @Mock
    UserServiceImpl userService;
    @Mock
    MeterReadingServiceImpl meterReadingService;
    @Mock
    UserActivityServiceImpl activityService;
    @Mock
    DictionaryServiceImpl dictionaryService;
    @InjectMocks
    Controller controller;

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object")
//    void registerUser() {
//        controller.registerUser("dummy", "dummy", "dummy", "dummy", "dummy");
//
//        verify(userService, times(1)).registerUser("dummy", "dummy",
//                "dummy", "dummy", "dummy");
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object")
//    void registerAdmin() {
//        controller.registerAdmin("dummy", "dummy", "dummy", "dummy", "dummy", "dummy",
//                "dummy");
//
//        verify(userService, times(1)).registerUser("dummy", "dummy", "dummy",
//                "dummy", "dummy", "dummy", "dummy");
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns user")
//    void authorizeUser() {
//        User user = mock(User.class);
//        when(userService.authenticateUser(anyString(), anyString()))
//                .thenReturn(user);
//        User testUser = controller.authenticateUser("dummy", "dummy");
//
//        assertThat(testUser).isEqualTo(user);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns hashset")
//    void getUserList() {
//        Set<User> users = mock(HashSet.class);
//        when(userService.getAllUsers()).thenReturn(users);
//
////        Set<User> testUsers = controller.getAllUsers();
////
////        assertThat(testUsers).isEqualTo(users);
//    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns list")
    void getReadingList() {
        List<List<MeterReadingSubmitDto>> readings = mock(ArrayList.class);
        when(meterReadingService.getAllReadingsList(2)).thenReturn(readings);

        List<List<MeterReadingSubmitDto>> testReadings = controller.getAllReadingsList(2);

        assertThat(testReadings).isEqualTo(readings);
    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns user")
//    void getActivityList() {
//        List<UserAction> activitiesDummy = new ArrayList<>();
//        when(activityService.getUserActivitiesList()).thenReturn(activitiesDummy);
//
//        List<UserAction> activities = controller.getUserActivitiesList();
//
//        assertThat(activities).isEqualTo(activitiesDummy);
//    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object")
    void addUtilityType() {
        String utilityName = "ELECTRICITY";
        controller.addUtilityTypeToDictionary(utilityName);

        verify(dictionaryService, times(1))
                .addUtilityTypeToDictionary(utilityName);
    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object")
//    void addReading() {
//        User user = mock(User.class);
//        MeterReading reading = mock(MeterReadingDto.class);
//        controller.submitNewMeterReading(reading);
//
//        verify(meterReadingService, times(1)).submitNewMeterReading(reading);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object")
//    void getActualReadingByUser() {
//        User userDummy = mock(User.class);
//        List<MeterReading> readingDummy = mock(ArrayList.class);
//        when(meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(userDummy))
//                .thenReturn(readingDummy);
//
//        List<MeterReading> meterReading = controller.getActualMeterReadingsOnAllUtilitiesByUser(userDummy);
//
//        assertThat(meterReading).isEqualTo(readingDummy);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns user")
//    void getReadingsHistoryByUser() {
//        User userDummy = mock(User.class);
//        List<List<MeterReadingDto>> readingsDummy = mock(ArrayList.class);
//        when(meterReadingService.getMeterReadingsHistoryByUser(1, 1))
//                .thenReturn(readingsDummy);
//
//        List<List<MeterReading>> readings = controller.getMeterReadingsHistoryByUser(userDummy, 1);
//
//        assertThat(readings).isEqualTo(readingsDummy);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns user")
//    void getReadingsForMonthByUser() {
//        User userDummy = mock(User.class);
//        List<MeterReading> readingDummy = mock(ArrayList.class);
//        when(meterReadingService.getReadingsForMonthByUser(userDummy, 2024, 1))
//                .thenReturn(readingDummy);
//
//        List<MeterReading> reading = controller.getReadingsForMonthByUser(userDummy, 2024, 1);
//
//        assertThat(reading).isEqualTo(readingDummy);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object")
//    void recordExit() {
//        User userDummy = mock(User.class);
//        controller.recordExit(userDummy);
//
//        verify(userService, times(1)).recordExit(userDummy);
//    }
}