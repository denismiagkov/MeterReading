package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewActualDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewForMonthDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.DictionaryServiceImpl;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.MeterReadingServiceImpl;
import com.denmiagkov.meter.infrastructure.in.validator.exception.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.UtilityTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeterReadingDtoValidatorImplTest {
    @Mock
    DictionaryService dictionaryService;
    @Mock
    MeterReadingService meterReadingService;
    @InjectMocks
    MeterReadingDtoValidatorImpl validator;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Returns true when dictionary size is more than utility Id")
    void isValidMeterReadingUtilityType_ReturnsTrue() {
        MeterReadingSubmitDto newMeterReading = mock(MeterReadingSubmitDto.class);
        Map<Integer, String> dictionary = mock(HashMap.class);
        when(dictionaryService.getUtilitiesDictionary()).thenReturn(dictionary);
        when(dictionary.size()).thenReturn(3);
        when(newMeterReading.getUtilityId()).thenReturn(2);

        boolean result = validator.isValidMeterReadingUtilityType(newMeterReading);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Throws UtilityTypeNotFoundException when dictionary size is less than utility Id")
    void isValidMeterReadingUtilityType_ThrowsException() {
        MeterReadingReviewActualDto newMeterReading = mock(MeterReadingReviewActualDto.class);
        Map<Integer, String> dictionary = mock(HashMap.class);
        when(dictionaryService.getUtilitiesDictionary()).thenReturn(dictionary);
        when(dictionary.size()).thenReturn(3);
        when(newMeterReading.getUtilityId()).thenReturn(5);

        assertThatThrownBy(() -> validator.isValidMeterReadingUtilityType(newMeterReading))
                .isInstanceOf(UtilityTypeNotFoundException.class)
                .hasMessage("This type of utilities is not registered!");
    }

    @Test
    @DisplayName("Returns true when meter reading date is not later than now")
    void isValidMonth_ReturnsTrue() {
        MeterReadingReviewForMonthDto requestDto = mock(MeterReadingReviewForMonthDto.class);
        when(requestDto.getMonth()).thenReturn(2);
        when(requestDto.getYear()).thenReturn(2024);

        boolean result = validator.isValidMonth(requestDto);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Throws exception when meter reading date is later than now")
    void isValidMonth_ThrowsException() {
        MeterReadingReviewForMonthDto requestDto = mock(MeterReadingReviewForMonthDto.class);
        when(requestDto.getMonth()).thenReturn(9);
        when(requestDto.getYear()).thenReturn(2024);

        assertThatThrownBy((() -> validator.isValidMonth(requestDto)))
                .isInstanceOf(InvalidDateException.class);
    }

    @Test
    @DisplayName("Throws exception when month value is incorrect")
    void isValidMonth_ThrowsException_IncorrectMonthValue() {
        MeterReadingReviewForMonthDto requestDto = mock(MeterReadingReviewForMonthDto.class);
        when(requestDto.getMonth()).thenReturn(13);

        assertThatThrownBy((() -> validator.isValidMonth(requestDto)))
                .isInstanceOf(InvalidDateException.class);
    }
}