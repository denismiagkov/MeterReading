package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализовывает логику взаимодействия с базой данных по поводу действий пользователей в приложении
 */


public class ActivityRepositoryImpl implements ActivityRepository {
    public static final ActivityRepositoryImpl INSTANCE = new ActivityRepositoryImpl();
    /**
     * SQL-запрос на добавление одного пользовательского действия в базу данных
     */
    private static final String ADD_ACTIVITY = """
            INSERT INTO meter_service.activities (user_id, date, action)
            VALUES (?, ?, ?);
            """;
    /**
     * SQL-запрос на выборку из базы данных всех записей о пользовательских действиях
     */
    private static final String GET_ACTIVITIES_LIST = """
            SELECT id, user_id, date, action
            FROM meter_service.activities;
            """;

    private ActivityRepositoryImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addActivity(UserAction userAction) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ADD_ACTIVITY)) {
            statement.setInt(1, userAction.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(userAction.getDateTime()));
            statement.setString(3, String.valueOf(userAction.getAction()));
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserAction> getActivitiesList() {
        List<UserAction> userActivitiesList = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ACTIVITIES_LIST)) {
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                UserAction userAction = getActivity(queryResult);
                userActivitiesList.add(userAction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userActivitiesList;
    }

    private UserAction getActivity(ResultSet queryResult) throws SQLException {
        int id = queryResult.getInt("id");
        int userId = queryResult.getInt("user_id");
        LocalDateTime dateTime = queryResult.getTimestamp("date")
                .toLocalDateTime();
        ActionType action = ActionType.valueOf(queryResult.getString("action"));
        return new UserAction(id, userId, dateTime, action);
    }
}
