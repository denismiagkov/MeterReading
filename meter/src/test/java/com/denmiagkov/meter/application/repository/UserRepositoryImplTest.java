package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.IT.config.PostgresExtension;
import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.repository.impl.UserRepositoryImpl;
import com.denmiagkov.meter.application.service.exceptions.AuthenticationFailedException;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(PostgresExtension.class)
@DirtiesContext
class UserRepositoryImplTest {
    @Autowired
    UserRepositoryImpl userRepository;
    @Autowired
    ConnectionManager connectionManager;
    Connection connection;
    User user;
    Set<User> users;

    @BeforeEach
    void setUp() throws SQLException {
        connection = connectionManager.open();
        connection.setAutoCommit(false);
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        userRepository.saveUser(user);
        users = userRepository.findAllUsers(Pageable.of(0, 10));
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    @DisplayName("Method adds new common user in database")
    void addUser_AddSimpleUser() {
        assertAll(
                () -> assertThat(users.size()).isEqualTo(4),
                () -> assertThat(users.contains(user)).isTrue());
    }

    @Test
    @DisplayName("Returns true if user exists in database")
    void isExistUser_ReturnsTrue() {
        assertThat(users.contains(user)).isTrue();
    }

    @Test
    @DisplayName("Returns false if user doesn't exist in database")
    void isExistUser_ReturnsFalse() {
        User user2 = new User("Paul", "11-22-33", "spb", "testUser", "123");

        assertThat(users.contains(user2)).isFalse();
    }

    @Test
    @DisplayName("Returns true if there IS user in database with the same login")
    void isExistLogin_ReturnsTrue() {
        List<String> logins = users.stream()
                .map(e -> e.getLogin()).toList();

        assertThat(logins.contains("user")).isTrue();
    }

    @Test
    @DisplayName("Returns false if there IS NO user in database with the same login")
    void isExistLogin_ReturnsFalse() {
        List<String> logins = users.stream()
                .map(e -> e.getLogin()).toList();

        assertThat(logins.contains("user100")).isFalse();
    }

    @Test
    @DisplayName("Returns true if there IS user in database with given login and password")
    void authenticateUser_ReturnsUser() {
        User user1 = userRepository.findUserByLogin("user")
                .orElseThrow(AuthenticationFailedException::new);

        assertThat(user1).isEqualTo(user);
    }

    @Test
    @DisplayName("Throws exception if given password doesn't correlate to user's password in database")
    void authorizeUser_ThrowsExceptionIfPasswordIsIncorrect() {
        assertThatThrownBy(() -> userRepository.findUserByLogin("user100")
                .orElseThrow(AuthenticationFailedException::new))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("Ошибка аутентификации: неверные логин и пароль!");
    }

    @Test
    @Disabled
    @DisplayName("Returns collection of all users in database")
    void getUsers() {
        User user2 = new User("Paul", "11-22-33", "user2", "msk", "321");
        userRepository.saveUser(user2);

        users = userRepository.findAllUsers(Pageable.of(0, 10));

        assertThat(users).isNotNull().hasSize(5);
    }
}