package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.Reading;
import com.denmiagkov.meter.domain.User;

import java.util.List;

public interface ReadingService {
    void addUtilityType(String newUtility);

    void submitNewReading(User user, Reading reading);

    List<Reading> getAllReadingsList();

    Reading getActualReadingByUser(User user);

    List<Reading> getReadingsHistoryByUser(User user);

    Reading getReadingsForMonthByUser(User user, int year, int month);
}
