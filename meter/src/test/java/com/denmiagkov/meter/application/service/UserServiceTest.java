package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.Storage;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.ActivityType;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    UserService userService;
    Storage storage;
    User user;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        userService = new UserService(storage);
        user = new User("John", "11-22-33", "Moscow", "user", "123");
    }

    @Test
    void registerUser_ReturnsUser() {
        when(storage.isExistUser(user)).thenReturn(false);
        when(storage.isExistLogin(anyString())).thenReturn(false);
        when(storage.addUser(user)).thenReturn(true);

        User user1 = userService.registerUser("John", "11-22-33", "Moscow", "user", "123");

        assertThat(user1).isEqualTo(user);
    }

    @Test
    void registerUser_ThrowsExceptionIfTheSameUserAlreadyRegistered() {
        when(storage.isExistUser(user)).thenReturn(true);
        when(storage.isExistLogin(anyString())).thenReturn(false);
        when(storage.addUser(user)).thenReturn(true);

      //  User user1 = userService.registerUser("John", "11-22-33", "Moscow", "user", "123");

        assertThatThrownBy(() -> userService.registerUser(
                "John", "11-22-33", "Moscow", "user", "123"))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void registerUser_ThrowsExceptionIfTheSameLiginIsUsedByAnotherUser() {
        when(storage.isExistUser(user)).thenReturn(false);
        when(storage.isExistLogin(anyString())).thenReturn(true);
        when(storage.addUser(user)).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(
                "John", "11-22-33", "Moscow", "user", "123"))
                .isInstanceOf(LoginAlreadyInUseException.class);
    }

    @Test
    void registerAdmin_ReturnsAdmin() {
        when(storage.isExistUser(user)).thenReturn(false);
        when(storage.isExistLogin(anyString())).thenReturn(false);
        when(storage.addUser(user)).thenReturn(true);
        User user2 = new User("John", "11-22-33",  "user", "123", "true", "123admin");

        User user1 = userService.registerUser("John", "11-22-33",  "user", "123", "true", "123admin");

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void authorizeUser() {
        when(storage.authorizeUser(anyString(), anyString()))
                .thenReturn(user);

        User testUser = userService.authorizeUser("dummy", "dummy");

        assertThat(testUser).isEqualTo(user);

    }

    @Test
    void getAllUsers() {
        Set<User> usersDummy = new HashSet<>();
        when(storage.getUsers()).thenReturn(usersDummy);

        Set<User> users = userService.getAllUsers();

        assertThat(users).isEqualTo(usersDummy);
    }

    @Test
    void getUserActivitiesList() {
        List<Activity> activitiesDummy = new ArrayList<>();
        when(storage.getActivitiesList()).thenReturn(activitiesDummy);

        List<Activity> activities = userService.getUserActivitiesList();

        assertThat(activities).isEqualTo(activitiesDummy);
    }
}