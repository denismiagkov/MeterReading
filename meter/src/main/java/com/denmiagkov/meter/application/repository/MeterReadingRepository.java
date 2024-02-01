package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за хранение данных о показаниях счетчиков в памяти приложения
 */
@Getter
@NoArgsConstructor
public class MeterReadingRepository {
    /**
     * Список всех переданных показаний
     */
    private static final List<MeterReading> METER_METER_READINGS = new ArrayList<>();


    /**
     * Метод добавления новых показаний в коллекцию
     *
     * @param reading новые показания счетчика
     */
    public void addNewReading(MeterReading reading) {
        METER_METER_READINGS.add(reading);
    }

    /**
     * Метод просмотра актуальных (последних переданных) показаний счетчиков
     *
     * @param user Пользователь
     * @return Reading последние переданные показания счетчиков
     */
    public MeterReading getLastReading(User user) {
        return METER_METER_READINGS.stream()
                .filter(e -> e.getUserId().equals(user.getId()))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    /**
     * Метод получения всех переданных показаний
     *
     * @return List<Reading> Список всех переданных показаний
     */
    public List<MeterReading> getAllReadingsList() {
        return METER_METER_READINGS;
    }

    /**
     * Метод получения истории подачи показаний конкретным пользователем
     *
     * @param user Пользователь
     * @return List<Reading> Список показаний, поданных указанным пользователем
     */
    public List<MeterReading> getReadingsHistory(User user) {
        return METER_METER_READINGS.stream()
                .filter(e -> e.getUserId().equals(user.getId()))
                .toList();
    }

    /**
     * Метод получения показаний, переданных указанным пользователем, в определенный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return Reading Показания счетчика пользователя за указанные год и месяц
     */
    public MeterReading getReadingsForMonthByUser(User user, int year, int month) {
        return METER_METER_READINGS.stream()
                .filter(e ->
                        ((e.getDate().getYear() == year) && (e.getDate().getMonthValue() == month)))
                .findFirst()
                .orElse(null);
    }
}
