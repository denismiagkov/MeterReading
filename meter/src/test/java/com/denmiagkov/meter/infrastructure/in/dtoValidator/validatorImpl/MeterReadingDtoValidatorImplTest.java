package com.denmiagkov.meter.infrastructure.in.dtoValidator.validatorImpl;

import com.denmiagkov.meter.application.dto.incoming.ReviewActualMeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.SubmitNewMeterReadingDto;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl.MeterReadingDtoValidatorImpl;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.UtilityTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        SubmitNewMeterReadingDto newMeterReading = mock(SubmitNewMeterReadingDto.class);
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
        ReviewActualMeterReadingDto newMeterReading = mock(ReviewActualMeterReadingDto.class);
        Map<Integer, String> dictionary = mock(HashMap.class);
        when(dictionaryService.getUtilitiesDictionary()).thenReturn(dictionary);
        when(dictionary.size()).thenReturn(3);
        when(newMeterReading.getUtilityId()).thenReturn(5);

        assertThatThrownBy(() -> validator.isValidMeterReadingUtilityType(newMeterReading))
                .isInstanceOf(UtilityTypeNotFoundException.class)
                .hasMessage("Data input error: This type of utilities is not registered!");
    }

    @Test
    @DisplayName("Returns true when meter reading date is not later than now")
    void isValidMonth_ReturnsTrue() {
        boolean result = validator.isValidMonth(2, 2024);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Throws exception when meter reading date is later than now")
    void isValidMonth_ThrowsException() {
        assertThatThrownBy((() -> validator.isValidMonth(9, 2024)))
                .isInstanceOf(InvalidDateException.class);
    }

    @Test
    @DisplayName("Throws exception when month value is incorrect")
    void isValidMonth_ThrowsException_IncorrectMonthValue() {
        assertThatThrownBy((() -> validator.isValidMonth(13, 2023)))
                .isInstanceOf(InvalidDateException.class);
    }
}