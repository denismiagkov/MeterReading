package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.MeterReadingRepository;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MeterReadingServiceImplTest {
    MeterReadingService meterReadingService;
    MeterReadingRepository meterReadingRepository;
    UserActivityService activityService;
    User user;

    @BeforeEach
    void setUp() {
        meterReadingRepository = mock(MeterReadingRepository.class);
        activityService = mock(UserActivityService.class);
        meterReadingService = new MeterReadingServiceImpl(meterReadingRepository, activityService);
        user = new User("John", "11-22-33", "Moscow", "user", "123");
    }
//
//    @Test
//    void submitNewReading() {
//        MeterReading reading = mock(MeterReading.class);
//
//        meterReadingService.submitNewReading(user, reading);
//
//        verify(meterReadingRepository, times(1)).addNewMeterReading(reading);
//        verify(activityService, times(1))
//                .addActivity(any(Activity.class));
//    }
//
//    @Test
//    void getAllReadingsList() {
//        List<MeterReading> readingsListDummy = mock(ArrayList.class);
//        List<List<MeterReading>> readingsPagesDummy = ListUtils.partition(readingsListDummy, 2);
//        when(meterReadingRepository.getAllMeterReadings()).thenReturn(readingsListDummy);
//
//        List<List<MeterReading>> allTheReadings = meterReadingService.getAllReadingsList(2);
//
//        assertThat(allTheReadings).isEqualTo(readingsPagesDummy);
//    }
//
//    @Test
//    void getActualReadingByUser() {
//        MeterReading readingDummy = mock(MeterReading.class);
//        when(meterReadingRepository.getActualMeterReadingOnExactUtility(user)).thenReturn(readingDummy);
//
//        MeterReading reading = meterReadingService.getActualMeterReadingOnExactUtilityByUser(user);
//
//        verify(activityService, times(1))
//                .addActivity(any(Activity.class));
//        assertThat(reading).isEqualTo(readingDummy);
//    }
//
//    @Test
//    void getReadingsHistoryByUser() {
//        List<MeterReading> readingHistoryListDummy = mock(ArrayList.class);
//        List<List<MeterReading>> readingHistoryPagesDummy = ListUtils.partition(readingHistoryListDummy, 1);
//        when(meterReadingRepository.getMeterReadingsHistory(user)).thenReturn(readingHistoryListDummy);
//
//        List<List<MeterReading>> readingsHistory = meterReadingService.getMeterReadingsHistoryByUser(user, 1);
//
//        verify(activityService, times(1))
//                .addActivity(any(Activity.class));
//        assertThat(readingsHistory).isEqualTo(readingHistoryPagesDummy);
//    }
//
//    @Test
//    void getReadingsForMonthByUser() {
//        MeterReading readingDummy = mock(MeterReading.class);
//        when(meterReadingRepository.getMeterReadingsForExactMonthByUser(user, 2023, 11))
//                .thenReturn(readingDummy);
//
//        MeterReading reading = meterReadingService.getReadingsForMonthByUser(user, 2023, 11);
//
//        verify(activityService, times(1))
//                .addActivity(any(Activity.class));
//        assertThat(reading).isEqualTo(readingDummy);
//    }
}