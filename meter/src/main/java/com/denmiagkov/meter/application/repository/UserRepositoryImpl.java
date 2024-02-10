package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Класс, отвечающий за реализацию логики взаимодействия с базой данных по поводу сведений о пользователях
 */
@Getter
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {

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
    private static final String GET_USER_BY_LOGIN = """
            SELECT id, name, phone, address, role, login, password
            FROM meter_service.users
            WHERE login = ?;
            """;

    /**
     * SQL-запрос на выборку из таблицы всех записей о пользователях
     */
    private static final String GET_ALL_USERS = """
            SELECT id, name, phone, address, role, login, password
            FROM meter_service.users;
            """;

    /**
     * {@inheritDoc}
     */
    @Override
    public int addUser(User user) {
        try (Connection connection = ConnectionManager.open();
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
        try (Connection connection = ConnectionManager.open();
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
        try (Connection connection = ConnectionManager.open();
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
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statementFindUserByLoginAndPassword =
                     connection.prepareStatement(GET_USER_BY_LOGIN)) {
            statementFindUserByLoginAndPassword.setString(1, login);
            ResultSet queryResult = statementFindUserByLoginAndPassword.executeQuery();
            if (queryResult.next()) {
                user = getUser(queryResult);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(user);
    }
//    @Override
//    public User authenticateUser(String login, String password) {
//        try (Connection connection = ConnectionManager.open();
//             PreparedStatement preparedStatementGetUsers = connection.prepareStatement(GET_ALL_USERS);
//             PreparedStatement preparedStatementFindUser = connection.prepareStatement(AUTHENTICATE_USER)) {
//            ResultSet queryResult = preparedStatementGetUsers.executeQuery();
//            while (queryResult.next()) {
//                String queryLogin = queryResult.getString("login");
//                String queryPassword = queryResult.getString("password");
//                if (login.equals(queryLogin) &&
//                    password.equals(queryPassword)) {
//                    return getUser(queryResult);
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        throw new AuthenticationFailedException();
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<User> getAllUsers() {
        Set<User> users = new HashSet<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS)) {
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                User user = getUser(queryResult);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    /**
     * Метод возвращает пользователя на основе соответствующей записи в базе данных
     *
     * @param queryResult Запись о пользователе в базе данных
     * @return User Пользователь
     * @throws SQLException
     */ public User getUser(ResultSet queryResult) throws SQLException {
        return User.builder()
                .id(queryResult.getInt("id"))
                .name(queryResult.getString("name"))
                .phone(queryResult.getString("phone"))
                .address(queryResult.getString("address"))
                .role(UserRole.valueOf(queryResult.getString("role")))
                .login(queryResult.getString("login"))
                .password(queryResult.getString("password"))
                .build();
    }
}
