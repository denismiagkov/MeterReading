package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.UserActivity;
import com.denmiagkov.meter.domain.ActivityType;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Класс реализовывает логику взаимодействия с базой данных по поводу действий пользователей в приложении
 * */
@Getter
@NoArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepository {
/**
 *  SQL-запрос на добавление одного пользовательского действия в базу данных
 * */
    private static final String ADD_ACTIVITY = """
            INSERT INTO meter_service.activities (user_id, date, action)
            VALUES (?, ?, ?);
            """;
/**
 * SQL-запрос на выборку из базы данных всех записей о пользовательских действиях
 * */
    private static final String GET_ACTIVITIES_LIST = """
            SELECT id, user_id, date, action
            FROM meter_service.activities;
            """;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addActivity(UserActivity userActivity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ADD_ACTIVITY)) {
            statement.setInt(1, userActivity.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(userActivity.getDateTime()));
            statement.setString(3, String.valueOf(userActivity.getAction()));
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public List<UserActivity> getActivitiesList() {
        List<UserActivity> userActivitiesList = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ACTIVITIES_LIST)) {
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                UserActivity userActivity = getActivity(queryResult);
                userActivitiesList.add(userActivity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userActivitiesList;
    }

    private UserActivity getActivity(ResultSet queryResult) throws SQLException {
        return UserActivity.builder()
                .id(queryResult.getInt("id"))
                .userId(queryResult.getInt("user_id"))
                .dateTime(queryResult.getTimestamp("date")
                        .toLocalDateTime())
                .action(ActivityType.valueOf(queryResult.getString("action")))
                .build();
    }
}
