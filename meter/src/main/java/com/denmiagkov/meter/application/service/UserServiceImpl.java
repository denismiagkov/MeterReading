package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.domain.*;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * Сервис пользователя
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * Репозиторий данных о пользователе
     */
    private final UserRepository userRepository;
    /**
     * Сервис данных о действиях пользователя
     */
    private final UserActivityService activityService;

    /**
     * {@inheritDoc}
     */
    @Override
    public User registerUser(String name, String phone, String address, String login, String password) {
        User user = new User(name, phone, address, login, password);
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(login)) {
                userRepository.addUser(user);
                Activity activity = new Activity(user, ActivityType.REGISTRATION);
                activityService.addActivity(activity);
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
    public User registerUser(String name, String phone, String login, String password,
                             String inputIsAdmin, String adminPassword) {
        User user = new User(name, phone, login, password, inputIsAdmin, adminPassword);
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
    public User authorizeUser(String login, String password) {
        User user = userRepository.authorizeUser(login, password);
        if (user != null && !user.getRole().equals(UserRole.ADMIN)) {
            Activity activity = new Activity(user, ActivityType.AUTHORIZATION);
            activityService.addActivity(activity);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<User> getAllUsers() {
        return userRepository.getUsers();
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
