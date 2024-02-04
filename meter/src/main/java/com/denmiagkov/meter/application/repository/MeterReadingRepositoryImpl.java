package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализует логику взаимодействия с базой данных, связанную с показаниями счетчиков
 */
@Getter
@NoArgsConstructor
public class MeterReadingRepositoryImpl implements MeterReadingRepository {
    /**
     * SQL-запрос на добавление в базу данных нового показания счетчика
     */
    private static final String ADD_NEW_METER_READING = """
            INSERT INTO meter_service.meter_readings (user_id, date, utility_id, value)
            VALUES (?, ?, ?, ?);
            """;

    /**
     * SQL-запрос на получение из базы данных актуального показателя счетчика по конкретной услуге для одного пользователя
     */
    private static final String GET_ACTUAL_METER_READING_ON_EXACT_UTILITY_BY_USER = """
            SELECT *
            FROM meter_service.meter_readings
            WHERE user_id = ? AND utility_id = ?
            ORDER BY date desc
            LIMIT 1;
            """;
    /**
     * SQL-запрос на получение из базы данных актуальных показателей счетчиков по всем услугам для одного пользователя
     */
    private final static String GET_ACTUAL_METER_READINGS_ON_ALL_UTILITIES_BY_USER = """
            SELECT DISTINCT ON (utility_id) *
            FROM meter_service.meter_readings
            WHERE user_id = ?
            ORDER BY utility_id, date desc ;
            """;
    /**
     * SQL-запрос на выборку всех переданных показаний счетчиков всеми пользователями
     */
    private final static String GET_ALL_METER_READINGS_BY_ALL_USERS = """
            SELECT *
            FROM meter_service.meter_readings;
            """;
    /**
     * SQL-запрос на получение из базы данных истории переданных показаний счетчиков указанным пользователем
     */
    private final static String GET_HISTORY_OF_METER_READINGS_BY_USER = """
            SELECT *
            FROM meter_service.meter_readings
            WHERE user_id = ?;
            """;
    /**
     * SQL-запрос на получение всех переданных определенным пользователем показаний счетчиков за определенный месяц
     */
    private final static String GET_METER_READINGS_FOR_EXACT_MONTH_BY_USER = """
            SELECT *
            FROM meter_service.meter_readings
            WHERE user_id = ?
                AND extract(year from date) = ?
                AND  extract(month from date) = ?
            """;

    /***
     * {@inheritDoc}
     */
    @Override
    public void addNewMeterReading(MeterReading reading) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ADD_NEW_METER_READING)) {
            statement.setInt(1, reading.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(reading.getDate()));
            statement.setInt(3, reading.getUtilityId());
            statement.setDouble(4, reading.getValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> getActualMeterReadingsOnAllUtilitiesByUser(User user) {
        List<MeterReading> allActualMeterReadings = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ACTUAL_METER_READINGS_ON_ALL_UTILITIES_BY_USER)) {
            statement.setInt(1, user.getId());
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                allActualMeterReadings.add(getMeterReadingFromDatabase(queryResult));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allActualMeterReadings;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public MeterReading getActualMeterReadingOnExactUtility(User user, int utilityId) {
        MeterReading meterReading = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ACTUAL_METER_READING_ON_EXACT_UTILITY_BY_USER)) {
            statement.setInt(1, user.getId());
            statement.setInt(2, utilityId);
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                meterReading = getMeterReadingFromDatabase(queryResult);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meterReading;
    }

    /**
     * Метод получения объекта показания счетчика из выбранной записи из базы данных
     *
     * @param queryResult Полученная из базы данных запись о показании счетчика
     * @return MeterReading  Объект показания счетчика
     */
    private MeterReading getMeterReadingFromDatabase(ResultSet queryResult) throws SQLException {
        MeterReading meterReading;
        meterReading = MeterReading.builder()
                .id(queryResult.getInt("id"))
                .userId(queryResult.getInt("user_id"))
                .date(queryResult.getTimestamp("date")
                        .toLocalDateTime())
                .utilityId(queryResult.getInt("utility_id"))
                .value(queryResult.getDouble("value"))
                .build();
        return meterReading;
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> getAllMeterReadings() {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_METER_READINGS_BY_ALL_USERS)) {
            statement.setFetchSize(50);
            ResultSet queryResult = statement.executeQuery();
            List<MeterReading> userMeterReadingsByMonth = new ArrayList<>();
            while (queryResult.next()) {
                userMeterReadingsByMonth.add(getMeterReadingFromDatabase(queryResult));
            }
            return userMeterReadingsByMonth;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> getMeterReadingsHistory(User user) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_HISTORY_OF_METER_READINGS_BY_USER)) {
            statement.setInt(1, user.getId());
            statement.setFetchSize(50);
            ResultSet resultSet = statement.executeQuery();
            List<MeterReading> userMeterReadingsHistory = new ArrayList<>();
            while (resultSet.next()) {
                userMeterReadingsHistory.add(getMeterReadingFromDatabase(resultSet));
            }
            return userMeterReadingsHistory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> getMeterReadingsForExactMonthByUser(User user, int year, int month) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_METER_READINGS_FOR_EXACT_MONTH_BY_USER)) {
            statement.setInt(1, user.getId());
            statement.setInt(2, year);
            statement.setInt(3, month);
            ResultSet queryResult = statement.executeQuery();
            List<MeterReading> userMeterReadingsByMonth = new ArrayList<>();
            while (queryResult.next()) {
                userMeterReadingsByMonth.add(getMeterReadingFromDatabase(queryResult));
            }
            return userMeterReadingsByMonth;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
