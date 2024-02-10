package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.MeterReadingRepositoryImpl;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MeterReadingServiceImplTest {
    @Mock
    MeterReadingRepositoryImpl meterReadingRepository;
    @Mock
    UserActivityServiceImpl activityService;
    @InjectMocks
    MeterReadingServiceImpl meterReadingService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "11-22-33", "Moscow", "user", "123");
    }

//    @Test
//    @DisplayName("Method invokes fit methods in dependent classes objects")
//    void submitNewReading() {
//        MeterReading reading = mock(MeterReading.class);
//
//        meterReadingService.submitNewReading(user, reading);
//
//        verify(meterReadingRepository, times(1)).addNewMeterReading(reading);
//        verify(activityService, times(1))
//                .addActivity(any(UserAction.class));
//    }

//    @Test
//    @DisplayName("Dependent object returns paginated list")
//    void getAllReadingsList() {
//        List<MeterReadingDto> readingsListDummy = mock(ArrayList.class);
//        List<List<MeterReadingDto>> readingsPagesDummy = ListUtils.partition(readingsListDummy, 2);
//        when(meterReadingRepository.getAllMeterReadings()).thenReturn(readingsListDummy);
//
//        List<List<MeterReadingDto>> allTheReadings = meterReadingService.getAllReadingsList(2);
//
//        assertThat(allTheReadings).isEqualTo(readingsPagesDummy);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns meter reading")
//    void getActualReadingByUser() {
//        MeterReading readingDummy = mock(MeterReading.class);
//        when(meterReadingRepository.getActualMeterReadingOnExactUtility(user, 1))
//                .thenReturn(readingDummy);
//
//        MeterReading reading = meterReadingService.getActualMeterReadingOnExactUtilityByUser(user, 1);
//
//        verify(activityService, times(1))
//                .addActivity(any(UserAction.class));
//        assertThat(reading).isEqualTo(readingDummy);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns meter reading")
//    void getReadingsHistoryByUser() {
//        List<MeterReading> readingHistoryListDummy = mock(ArrayList.class);
//        List<List<MeterReading>> readingHistoryPagesDummy = ListUtils.partition(readingHistoryListDummy, 1);
//        when(meterReadingRepository.getMeterReadingsHistory(user)).thenReturn(readingHistoryListDummy);
//
//        List<List<MeterReading>> readingsHistory = meterReadingService.getMeterReadingsHistoryByUser(user, 1);
//
//        verify(activityService, times(1))
//                .addActivity(any(UserAction.class));
//        assertThat(readingsHistory).isEqualTo(readingHistoryPagesDummy);
//    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns meter readings list")
//    void getReadingsForMonthByUser() {
//        List<MeterReading> readingDummy = mock(ArrayList.class);
//        when(meterReadingRepository.getMeterReadingsForExactMonthByUser(user, 2023, 11))
//                .thenReturn(readingDummy);
//
//        List<MeterReading> reading = meterReadingService.getReadingsForMonthByUser(user, 2023, 11);
//
//        verify(activityService, times(1))
//                .addActivity(any(UserAction.class));
//        assertThat(reading).isEqualTo(readingDummy);
//    }
}