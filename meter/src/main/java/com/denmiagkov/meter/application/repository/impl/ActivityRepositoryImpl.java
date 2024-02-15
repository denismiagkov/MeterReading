package com.denmiagkov.meter.application.repository.impl;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализовывает логику взаимодействия с базой данных по поводу действий пользователей в приложении
 */
@Repository
public class ActivityRepositoryImpl implements ActivityRepository {
    /**
     * SQL-запрос на добавление одного пользовательского действия в базу данных
     */
    private static final String ADD_USER_ACTION = """
            INSERT INTO meter_service.activities (user_id, date, action)
            VALUES (?, ?, ?);
            """;
    /**
     * SQL-запрос на выборку из базы данных всех записей о пользовательских действиях
     */
    private static final String FIND_ALL_USERS_ACTIONS = """
            SELECT id, user_id, date, action
            FROM meter_service.activities
            LIMIT ? OFFSET ?;
            """;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addUserAction(UserAction userAction) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(ADD_USER_ACTION)) {
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
    public List<UserAction> findAllUsersActions(int pageSize, int page) {
        List<UserAction> userActivitiesList = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERS_ACTIONS)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, page);
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
