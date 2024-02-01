package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.exception.AuthenticationFailedException;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    UserRepository userRepository = new UserRepository();
    User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        userRepository.addUser(user);
    }

    @Test
    @Disabled
    void addUser_TestAddUser() {
        assertAll(
                () -> assertThat(userRepository.getUsers().size()).isEqualTo(1),
                () -> assertThat(userRepository.getUsers().contains(user)).isTrue());
    }

    @Test
    void addUser_TestAddAdmin() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "aDmin", "123admin");

        userRepository.addUser(user2);

        assertAll(
                () -> assertThat(userRepository.getUsers().size()).isEqualTo(2),
                () -> assertThat(userRepository.getUsers().contains(user2)).isTrue());
    }

    @Test
    void isExistUser_ReturnsTrue() {
        assertThat(userRepository.getUsers().contains(user)).isTrue();
    }

    @Test
    @Disabled
    void isExistUser_ReturnsFalse() {
        User user2 = new User("Paul", "11-22-33", "user2", "123",
                "true", "123admin");

        assertThat(userRepository.getUsers().contains(user2)).isFalse();
    }

    @Test
    void isExistLogin_ReturnsTrue() {
        List<String> logins = userRepository.getUsers().stream().map(e -> e.getLogin()).toList();

        assertThat(logins.contains("user")).isTrue();
    }

    @Test
    void isExistLogin_ReturnsFalse() {
        List<String> logins = userRepository.getUsers().stream().map(e -> e.getLogin()).toList();

        assertThat(logins.contains("user1")).isFalse();
    }

    @Test
    void authorizeUser_ReturnsUser() {
        User user1 = userRepository.authorizeUser("user", "123");

        assertThat(user1).isEqualTo(user);
    }

    @Test
    void authorizeUser_ThrowsExceptionIfPasswordIsIncorrect() {
        assertThatThrownBy(() -> userRepository.authorizeUser("user", "321"))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("Ошибка авторизации: пользователя с указанными логином и паролем не существует!");
    }

    @Test
    void getUsers() {
        User user2 = new User("Paul", "11-22-33", "user2", "321",
                "AdMiN", "123admin");
        userRepository.addUser(user);
        userRepository.addUser(user2);

        var users = userRepository.getUsers();

        assertThat(users).isNotNull().hasSize(2);
    }
}