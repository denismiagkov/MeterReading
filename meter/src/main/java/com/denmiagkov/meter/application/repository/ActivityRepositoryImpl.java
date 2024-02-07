package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.Activity;
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
    public boolean addActivity(Activity activity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ADD_ACTIVITY)) {
            statement.setInt(1, activity.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(activity.getDateTime()));
            statement.setString(3, String.valueOf(activity.getAction()));
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public List<Activity> getActivitiesList() {
        List<Activity> userActivitiesList = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ACTIVITIES_LIST)) {
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                Activity activity = getActivity(queryResult);
                userActivitiesList.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userActivitiesList;
    }

    private Activity getActivity(ResultSet queryResult) throws SQLException {
        return Activity.builder()
                .id(queryResult.getInt("id"))
                .userId(queryResult.getInt("user_id"))
                .dateTime(queryResult.getTimestamp("date")
                        .toLocalDateTime())
                .action(ActivityType.valueOf(queryResult.getString("action")))
                .build();
    }
}
