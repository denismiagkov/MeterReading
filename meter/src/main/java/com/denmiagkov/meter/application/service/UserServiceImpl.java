package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepositoryImpl;
import com.denmiagkov.meter.domain.*;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * Класс реализует логику обработки данных о пользователях
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * Репозиторий данных о пользователе
     */
    private final UserRepositoryImpl userRepository;
    /**
     * Сервис данных о действиях пользователя
     */
    private final UserActivityService activityService;

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerUser(String inputName, String inputPhone, String inputAddress,
                             String inputLogin, String inputPassword) {
        User user = User.builder()
                .name(inputName)
                .phone(inputPhone)
                .address(inputAddress)
                .role(UserRole.USER)
                .login(inputLogin)
                .password(inputPassword)
                .build();
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(inputLogin)) {
                int userId = userRepository.addUser(user);
                user.setId(userId);
                Activity activity = new Activity(user, ActivityType.REGISTRATION);
                activityService.addActivity(activity);
                return user;
            } else {
                throw new LoginAlreadyInUseException(inputLogin);
            }
        } else {
            throw new UserAlreadyExistsException(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerUser(String name, String phone, String address, String login, String password,
                             String inputIsAdmin, String adminPassword) {
        User user = new User(name, phone, address, login, password, inputIsAdmin, adminPassword);
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(login)) {
                userRepository.addUser(user);
                return user;
            } else {
                throw new LoginAlreadyInUseException(login);
            }
        } else {
            throw new UserAlreadyExistsException(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User authenticateUser(String login, String password) {
        User user = userRepository.authenticateUser(login, password);
        if (!user.getRole().equals(UserRole.ADMIN)) {
            Activity activity = new Activity(user, ActivityType.AUTHENTICATION);
            activityService.addActivity(activity);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordExit(User user) {
        Activity activity = new Activity(user, ActivityType.EXIT);
        activityService.addActivity(activity);
    }
}
