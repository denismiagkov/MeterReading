package com.denmiagkov.meter.application.repository.impl;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Класс, отвечающий за реализацию логики взаимодействия с базой данных по поводу сведений о пользователях
 */
@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final ConnectionManager connectionManager;

    /**
     * SQL-запрос на добавление пользователя в таблицу
     */
    private static final String ADD_USER = """
            INSERT INTO meter_service.users(name, phone, address, role, login, password)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    /**
     * SQL-запрос на проверку, содержится ли в таблице запись о соответствующем пользователе
     */
    private static final String IS_EXIST_USER = """
            SELECT id, name, phone, address, role, login, password
            FROM meter_service.users
            WHERE name = ? AND phone = ?;
            """;

    /**
     * SQL-запрос на проверку, содержится ли в таблице запись о пользователе с соответствующим логином
     */
    private static final String IS_EXIST_LOGIN = """
            SELECT id, name, phone, address, role, login, password
            FROM meter_service.users
            WHERE login = ?;
            """;

    /**
     * SQL-запрос на получение записи о пользователе, владеющем указанными логином и паролем
     */
    private static final String FIND_USER_BY_LOGIN = """
            SELECT id, name, phone, address, role, login, password
            FROM meter_service.users
            WHERE login = ?;
            """;

    /**
     * SQL-запрос на выборку из таблицы всех записей о пользователях
     */
    private static final String FIND_ALL_USERS = """
            SELECT id, name, phone, address, role, login, password
            FROM meter_service.users
            LIMIT ? OFFSET ?;
            """;

    /**
     * {@inheritDoc}
     */
    @Override
    public int saveUser(User user) {
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(
                     ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPhone());
            statement.setString(3, user.getAddress());
            statement.setString(4, String.valueOf(user.getRole()));
            statement.setString(5, user.getLogin());
            statement.setString(6, user.getPassword());
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()) {
                int userId = generatedKey.getInt("id");
                return userId;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistUser(User user) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(IS_EXIST_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistLogin(String login) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(IS_EXIST_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findUserByLogin(String login) {
        User user = null;
        try (Connection connection = connectionManager.open();
             PreparedStatement statementFindUserByLoginAndPassword =
                     connection.prepareStatement(FIND_USER_BY_LOGIN)) {
            statementFindUserByLoginAndPassword.setString(1, login);
            ResultSet queryResult = statementFindUserByLoginAndPassword.executeQuery();
            if (queryResult.next()) {
                user = findUser(queryResult);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<User> findAllUsers(Pageable pageable) {
        Set<User> users = new HashSet<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS)) {
            preparedStatement.setInt(1, pageable.getPageSize());
            preparedStatement.setInt(2, (pageable.getPage() * pageable.getPageSize()));
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                User user = findUser(queryResult);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    /**
     * Метод возвращает пользователя на основе соответствующей записи в базе данных
     *
     * @param queryResult Запись о пользователе в базе данных
     * @return User Пользователь
     * @throws SQLException
     */
    private User findUser(ResultSet queryResult) throws SQLException {
        int id = queryResult.getInt("id");
        String name = queryResult.getString("name");
        String phone = queryResult.getString("phone");
        String address = queryResult.getString("address");
        UserRole role = UserRole.valueOf(queryResult.getString("role"));
        String login = queryResult.getString("login");
        String password = queryResult.getString("password");
        return new User(id, name, phone, address, role, login, password);
    }
}
