package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    UserService userService;
    UserRepository userRepository;
    UserActivityService activityService;
    User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        activityService = mock(UserActivityServiceImpl.class);
        userService = new UserServiceImpl(userRepository, activityService);
        user = new User("John", "11-22-33", "Moscow", "user", "123");
    }

    @Test
    void registerUser_ReturnsUser() {
        when(userRepository.isExistUser(user)).thenReturn(false);
        when(userRepository.isExistLogin(anyString())).thenReturn(false);
        when(userRepository.addUser(user)).thenReturn(true);

        User user1 = userService.registerUser("John", "11-22-33", "Moscow", "user", "123");

        verify(activityService, times(1))
                .addActivity(any(Activity.class));
        assertThat(user1).isEqualTo(user);
    }

    @Test
    void registerUser_ThrowsExceptionIfTheSameUserAlreadyRegistered() {
        when(userRepository.isExistUser(user)).thenReturn(true);
        when(userRepository.isExistLogin(anyString())).thenReturn(false);
        when(userRepository.addUser(user)).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(
                "John", "11-22-33", "Moscow", "user", "123"))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void registerUser_ThrowsExceptionIfTheSameLiginIsUsedByAnotherUser() {
        when(userRepository.isExistUser(user)).thenReturn(false);
        when(userRepository.isExistLogin(anyString())).thenReturn(true);
        when(userRepository.addUser(user)).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(
                "John", "11-22-33", "Moscow", "user", "123"))
                .isInstanceOf(LoginAlreadyInUseException.class);
    }

    @Test
    void registerAdmin_ReturnsAdmin() {
        when(userRepository.isExistUser(user)).thenReturn(false);
        when(userRepository.isExistLogin(anyString())).thenReturn(false);
        when(userRepository.addUser(user)).thenReturn(true);
        User user2 = new User("John", "11-22-33", "user", "123", "admin", "123admin");

        User user1 = userService.registerUser("John", "11-22-33", "user", "123", "ADMIN", "123admin");

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void authorizeUser() {
        when(userRepository.authorizeUser(anyString(), anyString()))
                .thenReturn(user);

        User testUser = userService.authorizeUser("dummy", "dummy");

        verify(activityService, times(1))
                .addActivity(any(Activity.class));
        assertThat(testUser).isEqualTo(user);
    }

    @Test
    void getAllUsers() {
        Set<User> usersDummy = new HashSet<>();
        when(userRepository.getUsers()).thenReturn(usersDummy);

        Set<User> users = userService.getAllUsers();

        assertThat(users).isEqualTo(usersDummy);
    }



    @Test
    void recordExit() {
        userService.recordExit(user);
        verify(activityService, times(1))
                .addActivity(any(Activity.class));
    }
}