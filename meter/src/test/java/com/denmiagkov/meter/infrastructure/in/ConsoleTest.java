package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.exception.SubmitReadingOnTheSameMonthException;
import com.denmiagkov.meter.application.exception.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
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
    User user;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        console = new Console(controller);
    }

    @Test
    void start() {
    }



//
//    @Test
//    void checkMonth_ThrowsException() {
//        user = new User("John", "11-22-33", "Moscow", "user", "123");
//        Map<String, Double> meterValues = new HashMap<>();
//        meterValues.put("HEATING", 144.25);
//        meterValues.put("HOT_WATER", 37.1);
//        meterValues.put("COLD_WATER", 24.35);
//        MeterReading reading = new MeterReading(user, meterValues);
//
//        assertThatThrownBy(() -> Console.ConsoleValidator.checkMonth(reading))
//                .isInstanceOf(SubmitReadingOnTheSameMonthException.class)
//                .hasMessage("Показания могут подаваться не чаще одного раза в месяц!");
//    }
//
//    @Test
//    void checkMonth_ReturnsTrue() throws NoSuchFieldException, IllegalAccessException {
//        user = new User("John", "11-22-33", "Moscow", "user", "123");
//        Map<String, Double> meterValues = new HashMap<>();
//        meterValues.put("HEATING", 144.25);
//        meterValues.put("HOT_WATER", 37.1);
//        meterValues.put("COLD_WATER", 24.35);
//        MeterReading reading = new MeterReading(user, meterValues);
//
//        Class<?> clazz = MeterReading.class;
//        Field fieldDate = clazz.getDeclaredField("date");
//        fieldDate.setAccessible(true);
//        fieldDate.set(reading, LocalDateTime.now().minusDays(50));
//
//        assertThat(Console.ConsoleValidator.checkMonth(reading)).isTrue();
//    }
//
//    @Test
//    void checkPreviousMeterValue_ThrowsException() {
//        user = new User("John", "11-22-33", "Moscow", "user", "123");
//        Map<String, Double> meterValues = new HashMap<>();
//        meterValues.put("HEATING", 144.25);
//        meterValues.put("HOT_WATER", 37.1);
//        meterValues.put("COLD_WATER", 24.35);
//        MeterReading reading = new MeterReading(user, meterValues);
//        String utility = "HEATING";
//        Double meterValue = 100.00;
//
//        assertThatThrownBy(() -> Console.ConsoleValidator
//                .checkPreviousMeterValue(reading, utility, meterValue))
//                .isInstanceOf(NewMeterValueIsLessThenPreviousException.class)
//                .hasMessage("Недопустимое знаачение: новое показание счетчика меньше предыдущего!");
//    }
//
//    @Test
//    void checkPreviousMeterValue_ReturnsTrue() {
//        user = new User("John", "11-22-33", "Moscow", "user", "123");
//        Map<String, Double> meterValues = new HashMap<>();
//        meterValues.put("HEATING", 144.25);
//        meterValues.put("HOT_WATER", 37.1);
//        meterValues.put("COLD_WATER", 24.35);
//        MeterReading reading = new MeterReading(user, meterValues);
//        String utility = "HEATING";
//        Double meterValue = 200.00;
//
//        assertThat(Console.ConsoleValidator.checkPreviousMeterValue(reading, utility, meterValue))
//                .isTrue();
//    }

}
