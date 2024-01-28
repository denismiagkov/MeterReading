package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.ActivityType;
import com.denmiagkov.meter.domain.Reading;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.application.repository.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReadingServiceTest {
    ReadingService readingService;
    Storage storage;
    User user;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        readingService = new ReadingService(storage);
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        storage.addUser(user);
    }

    @Test
    void addUtilityType() {

    }

    @Test
    void submitNewReading() {
        User user1 = mock(User.class);
        Reading reading = mock(Reading.class);
        // Activity activity = mock(Activity.class);

        readingService.submitNewReading(user, reading);

        verify(storage, times(1)).addNewReading(reading);
        // verify(storage, times(1)).addActivity(activity);
    }

    @Test
    void getAllReadingsList(){
        List<Reading> readingsDummy = new ArrayList<>();
        when(storage.getAllReadingsList()).thenReturn(readingsDummy);

        List<Reading> allTheReadings = readingService.getAllReadingsList();

        assertThat(allTheReadings).isEqualTo(readingsDummy);
    }

    @Test
    void getActualReadingByUser() {
        Reading readingDummy = mock(Reading.class);
        when(storage.getLastReading(user)).thenReturn(readingDummy);

        Reading reading = readingService.getActualReadingByUser(user);

        assertThat(reading).isEqualTo(readingDummy);
    }

    @Test
    void getReadingsHistoryByUser() {
        List<Reading> readingHistoryDummy = new ArrayList<>();
        when(storage.getReadingsHistory(user)).thenReturn(readingHistoryDummy);

        List<Reading> readingsHistory = readingService.getReadingsHistoryByUser(user);

        assertThat(readingsHistory).isEqualTo(readingHistoryDummy);
    }

    @Test
    void getReadingsForMonthByUser() {
        Reading readingDummy = mock(Reading.class);
        when(storage.getReadingsForMonthByUser(user, 2023, 11))
                .thenReturn(readingDummy);

        Reading reading = readingService.getReadingsForMonthByUser(user, 2023, 11);

        assertThat(reading).isEqualTo(readingDummy);
    }
}