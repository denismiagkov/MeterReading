package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.exception.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.application.exception.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ConsoleTest {
    Console console;
    Controller controller;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        console = new Console(controller);
    }

    @Test
    @DisplayName("Throws exception when the last and new meter readings are submitted on the same month")
    void checkMonth_ThrowsException() {
        MeterReading meterReading = MeterReading.builder()
                .id(350)
                .userId(1)
                .date(LocalDateTime.now().minusHours(1))
                .utilityId(1)
                .value(1500)
                .build();


        assertThatThrownBy(() -> Console.ConsoleValidator.checkMonth(meterReading))
                .isInstanceOf(SubmitReadingOnTheSameMonthException.class)
                .hasMessage("Показания могут подаваться не чаще одного раза в месяц!");
    }

    @Test
    @DisplayName("Returns true when the last and new meter readings are submitted on different months")
    void checkMonth_ReturnsTrue() throws NoSuchFieldException, IllegalAccessException {
        MeterReading meterReading = MeterReading.builder()
                .id(350)
                .userId(1)
                .date(LocalDateTime.now().minusDays(32))
                .utilityId(1)
                .value(1500)
                .build();

        assertThat(Console.ConsoleValidator.checkMonth(meterReading)).isTrue();
    }

    @Test
    @DisplayName("Throws exception when new meter reading is less than actual")
    void checkPreviousMeterValue_ThrowsException() {
        MeterReading meterReading = MeterReading.builder()
                .id(350)
                .userId(1)
                .date(LocalDateTime.now().minusDays(32))
                .utilityId(1)
                .value(100)
                .build();

        assertThatThrownBy(() -> Console.ConsoleValidator
                .checkPreviousMeterValue(meterReading, 75.0))
                .isInstanceOf(NewMeterValueIsLessThenPreviousException.class)
                .hasMessage("Недопустимое значение: новое показание счетчика меньше предыдущего!");
    }

    @Test
    @DisplayName("Returns true when new meter reading is larger than actual")
    void checkPreviousMeterValue_ReturnsTrue() {
        MeterReading meterReading = MeterReading.builder()
                .id(35)
                .userId(1)
                .date(LocalDateTime.now().minusHours(1))
                .utilityId(1)
                .value(100)
                .build();

        assertThat(Console.ConsoleValidator.checkPreviousMeterValue(meterReading, 125.0))
                .isTrue();
    }

}
