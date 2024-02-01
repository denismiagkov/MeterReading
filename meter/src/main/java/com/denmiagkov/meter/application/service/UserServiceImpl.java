package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.domain.*;
import com.denmiagkov.meter.application.repository.Storage;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Сервис пользователя
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final Storage storage;

    /**
     * Метод регистрации обычного пользователя
     *
     * @param name     Имя пользоыателя
     * @param phone    Телефон пользователя
     * @param address  Адрес пользователя
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return User
     * @throws LoginAlreadyInUseException при использовании логина, уже зарегистрированного в системе
     * @throws UserAlreadyExistsException при попытке повторной регистрации одного и того же пользователя
     */
    @Override
    public User registerUser(String name, String phone, String address, String login, String password) {
        User user = new User(name, phone, address, login, password);
        if (!storage.isExistUser(user)) {
            if (!storage.isExistLogin(login)) {
                storage.addUser(user);
                Activity activity = new Activity(user, ActivityType.REGISTRATION);
                storage.addActivity(activity);
                return user;
            } else {
                throw new LoginAlreadyInUseException(login);
            }
        } else {
            throw new UserAlreadyExistsException(user);
        }
    }

    /**
     * Метод регистрации администратора
     *
     * @param name          Имя пользоыателя
     * @param phone         Телефон пользователя
     * @param login         Логин пользователя
     * @param password      Пароль пользователя
     * @param inputIsAdmin  Подтверждение статуса администратора
     * @param adminPassword Единый пароль администратора
     * @return User
     * @throws LoginAlreadyInUseException при использовании логина, уже зарегистрированного в системе
     * @throws UserAlreadyExistsException при попытке повторной регистрации одного и того же пользователя
     */
    @Override
    public User registerUser(String name, String phone, String login, String password,
                             String inputIsAdmin, String adminPassword) {
        User user = new User(name, phone, login, password, inputIsAdmin, adminPassword);
        if (!storage.isExistUser(user)) {
            if (!storage.isExistLogin(login)) {
                storage.addUser(user);
                return user;
            } else {
                throw new LoginAlreadyInUseException(login);
            }
        } else {
            throw new UserAlreadyExistsException(user);
        }
    }

    /**
     * Метод авторизации пользователя
     *
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return User
     */
    @Override
    public User authorizeUser(String login, String password) {
        User user = storage.authorizeUser(login, password);
        if (user != null && !user.isAdmin()) {
            Activity activity = new Activity(user, ActivityType.AUTHORIZATION);
            storage.addActivity(activity);
        }
        return user;
    }

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User>
     */
    @Override
    public Set<User> getAllUsers() {
        return storage.getUsers();
    }

    /**
     * Метод возвращает список всех действий, совершенных пользователями
     *
     * @return List<Activity>
     */
    public List<Activity> getUserActivitiesList() {
        return storage.getActivitiesList();
    }

    /**
     * Метод фиксирует выход пользователя из приложения
     *
     * @param user Пользователь
     */
    @Override
    public void recordExit(User user) {
        Activity activity = new Activity(user, ActivityType.EXIT);
        storage.addActivity(activity);
    }
}
