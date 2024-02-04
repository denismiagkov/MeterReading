package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.exception.AuthenticationFailedException;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.utils.ConnectionManager;
import com.denmiagkov.meter.utils.LiquibaseManager;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserRepositoryImplTest {

    private static final int CONTAINER_PORT = 5432;
    private static final int LOCAL_PORT = 5431;
    UserRepositoryImpl userRepository;
    Connection connection;
    User user;
    Set<User> users;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("meter")
            .withUsername("meter")
            .withPassword("123")
            .withExposedPorts(CONTAINER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig()
                            .withPortBindings(new PortBinding(Ports.Binding.bindPort(LOCAL_PORT), new ExposedPort(CONTAINER_PORT)))
            ));

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() throws SQLException {
        LiquibaseManager.startLiquibase();
        userRepository = new UserRepositoryImpl();
        connection = ConnectionManager.open();
        connection.setAutoCommit(false);
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        userRepository.addUser(user);
        users = userRepository.getAllUsers();
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
    @DisplayName("Method adds new admin in database")
    void addUser_TestAddAdmin() {
        User user2 = new User("Paul", "11-22-33", "Moscow", "admin1", "123",
                "aDmin", "123admin");

        userRepository.addUser(user2);
        users = userRepository.getAllUsers();

        assertAll(
                () -> assertThat(users.size()).isEqualTo(5),
                () -> assertThat(users.contains(user2)).isTrue());
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
        User user1 = userRepository.authenticateUser("user", "123");

        assertThat(user1).isEqualTo(user);
    }

    @Test
    @DisplayName("Throws exception if given password doesn't correlate to user's password in database")
    void authorizeUser_ThrowsExceptionIfPasswordIsIncorrect() {
        assertThatThrownBy(() -> userRepository.authenticateUser("user", "321"))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("Ошибка авторизации: пользователя с указанными логином и паролем не существует!");
    }

    @Test
    @DisplayName("Returns collection of all users in database")
    void getUsers() {
        User user2 = new User("Paul", "11-22-33", "user2", "msk", "321",
                "AdMiN", "123admin");
        userRepository.addUser(user2);

        users = userRepository.getAllUsers();

        assertThat(users).isNotNull().hasSize(5);
    }
}