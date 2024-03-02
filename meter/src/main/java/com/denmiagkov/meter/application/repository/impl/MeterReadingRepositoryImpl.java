package com.denmiagkov.meter.application.repository.impl;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.repository.MeterReadingRepository;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализует логику взаимодействия с базой данных, связанную с показаниями счетчиков
 */
@Repository
@AllArgsConstructor
public class MeterReadingRepositoryImpl implements MeterReadingRepository {

    private final ConnectionManager connectionManager;

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
    private static final String FIND_ACTUAL_METER_READING_ON_EXACT_UTILITY_BY_USER = """
            SELECT id, user_id, date, utility_id, value
            FROM meter_service.meter_readings
            WHERE user_id = ? AND utility_id = ?
            ORDER BY date desc
            LIMIT 1;
            """;

    /**
     * SQL-запрос на получение из базы данных актуальных показателей счетчиков по всем услугам для одного пользователя
     */
    private static final String FIND_ACTUAL_METER_READINGS_ON_ALL_UTILITIES_BY_USER = """
            SELECT DISTINCT ON (utility_id) id, user_id, date, utility_id, value
            FROM meter_service.meter_readings
            WHERE user_id = ?
            ORDER BY utility_id, date desc ;
            """;

    /**
     * SQL-запрос на выборку всех переданных показаний счетчиков всеми пользователями
     */
    private static final String FIND_ALL_METER_READINGS_BY_ALL_USERS = """
            SELECT id, user_id, date, utility_id, value
            FROM meter_service.meter_readings
            LIMIT ? OFFSET ?;
            """;

    /**
     * SQL-запрос на получение из базы данных истории переданных показаний счетчиков указанным пользователем
     */
    private static final String FIND_HISTORY_OF_METER_READINGS_BY_USER = """
            SELECT id, user_id, date, utility_id, value
            FROM meter_service.meter_readings
            WHERE user_id = ?
            LIMIT ? OFFSET ?;
            """;

    /**
     * SQL-запрос на получение всех переданных определенным пользователем показаний счетчиков за определенный месяц
     */
    private static final String FIND_METER_READINGS_FOR_EXACT_MONTH_BY_USER = """
            SELECT id, user_id, date, utility_id, value
            FROM meter_service.meter_readings
            WHERE user_id = ?
                AND extract(year from date) = ?
                AND  extract(month from date) = ?
            """;

    /***
     * {@inheritDoc}
     */
    @Override
    public MeterReading addNewMeterReading(MeterReading meterReading) {
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ADD_NEW_METER_READING)) {
            statement.setInt(1, meterReading.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(meterReading.getDate()));
            statement.setInt(3, meterReading.getUtilityId());
            statement.setDouble(4, meterReading.getValue());
            statement.executeUpdate();
            return findActualMeterReadingOnExactUtility(meterReading.getUserId(), meterReading.getUtilityId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> findActualMeterReadingsOnAllUtilitiesByUser(int userId) {
        List<MeterReading> allActualMeterReadings = new ArrayList<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ACTUAL_METER_READINGS_ON_ALL_UTILITIES_BY_USER)) {
            statement.setInt(1, userId);
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                allActualMeterReadings.add(findMeterReadingFromDatabase(queryResult));
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
    public MeterReading findActualMeterReadingOnExactUtility(int userId, int utilityId) {
        MeterReading meterReading = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ACTUAL_METER_READING_ON_EXACT_UTILITY_BY_USER)) {
            statement.setInt(1, userId);
            statement.setInt(2, utilityId);
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                meterReading = findMeterReadingFromDatabase(queryResult);
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
    private MeterReading findMeterReadingFromDatabase(ResultSet queryResult) throws SQLException {
        int id = queryResult.getInt("id");
        int userId = queryResult.getInt("user_id");
        LocalDateTime date = queryResult.getTimestamp("date")
                .toLocalDateTime();
        int utilityId = queryResult.getInt("utility_id");
        double value = queryResult.getDouble("value");
        return new MeterReading(id, userId, date, utilityId, value);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public List<MeterReading> findAllMeterReadings(Pageable pageable) {
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_METER_READINGS_BY_ALL_USERS)) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPage() * pageable.getPageSize()));
            ResultSet queryResult = statement.executeQuery();
            List<MeterReading> userMeterReadingsByMonth = new ArrayList<>();
            while (queryResult.next()) {
                userMeterReadingsByMonth.add(findMeterReadingFromDatabase(queryResult));
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
    public List<MeterReading> findMeterReadingsHistory(int userId, Pageable pageable) {
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_HISTORY_OF_METER_READINGS_BY_USER)) {
            statement.setInt(1, userId);
            statement.setInt(2, pageable.getPageSize());
            statement.setInt(3, (pageable.getPage() * pageable.getPageSize()));
            ResultSet resultSet = statement.executeQuery();
            List<MeterReading> userMeterReadingsHistory = new ArrayList<>();
            while (resultSet.next()) {
                userMeterReadingsHistory.add(findMeterReadingFromDatabase(resultSet));
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
    public List<MeterReading> findMeterReadingsForExactMonthByUser(int userId, int year, int month) {
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_METER_READINGS_FOR_EXACT_MONTH_BY_USER)) {
            statement.setInt(1, userId);
            statement.setInt(2, year);
            statement.setInt(3, month);
            ResultSet queryResult = statement.executeQuery();
            List<MeterReading> userMeterReadingsByMonth = new ArrayList<>();
            while (queryResult.next()) {
                userMeterReadingsByMonth.add(findMeterReadingFromDatabase(queryResult));
            }
            return userMeterReadingsByMonth;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
