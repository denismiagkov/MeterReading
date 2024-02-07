package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepositoryImpl;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepositoryImpl userRepository;
    @Mock
    UserActivityServiceImpl activityService;
    @InjectMocks
    UserServiceImpl userService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "11-22-33", "Moscow", "user", "123");
    }

    @Test
    @DisplayName("If new user has NOT BEEN registered, " +
                 "method invokes appropriate method in dependent object, and dependent object returns userId")
    void registerUser_ReturnsNewUserId() {
        when(userRepository.isExistUser(user)).thenReturn(false);
        when(userRepository.isExistLogin(anyString())).thenReturn(false);
        when(userRepository.addUser(user)).thenReturn(anyInt());

        User user1 = userService.registerUser("John", "11-22-33", "Moscow", "user", "123");

        verify(activityService, times(1))
                .addActivity(any(Activity.class));
        assertThat(user1).isEqualTo(user);
    }

    @Test
    @DisplayName("If new user has BEEN registered, method invocation throws exception")
    void registerUser_ThrowsExceptionIfTheSameUserAlreadyRegistered() {
        when(userRepository.isExistUser(user)).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(
                "John", "11-22-33", "Moscow", "user", "123"))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("If new user's login is already in use, method invocation throws exception")
    void registerUser_ThrowsExceptionIfTheSameLoginIsUsedByAnotherUser() {
        when(userRepository.isExistLogin(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(
                "John", "11-22-33", "Moscow", "user", "123"))
                .isInstanceOf(LoginAlreadyInUseException.class);
    }

    @Test
    @DisplayName("If new admin entered correct admin password, dependent object returns userId")
    void registerAdmin_ReturnsAdmin() {
        when(userRepository.isExistUser(user)).thenReturn(false);
        when(userRepository.isExistLogin(anyString())).thenReturn(false);
        when(userRepository.addUser(user)).thenReturn(anyInt());
        User user2 = new User("John", "11-22-33", "Moscow", "user", "123", "admin", "123admin");

        User user1 = userService.registerUser("John", "11-22-33", "Moscow", "user", "123", "ADMIN", "123admin");

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("Method invokes appropriate method in dependent object, and dependent object returns user")
    void authorizeUser() {
        when(userRepository.authenticateUser(anyString(), anyString()))
                .thenReturn(user);

        User testUser = userService.authenticateUser("dummy", "dummy");

        verify(activityService, times(1))
                .addActivity(any(Activity.class));
        assertThat(testUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Dependent object method returns set of users")
    void getAllUsers() {
        Set<User> usersDummy = new HashSet<>();
        when(userRepository.getAllUsers()).thenReturn(usersDummy);

        Set<User> users = userService.getAllUsers();

        assertThat(users).isEqualTo(usersDummy);
    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object")
    void recordExit() {
        userService.recordExit(user);
        verify(activityService, times(1))
                .addActivity(any(Activity.class));
    }
}