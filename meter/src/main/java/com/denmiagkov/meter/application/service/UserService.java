package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.User;

import java.util.List;
import java.util.Set;

public interface UserService {

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
    User registerUser(String name, String phone, String address, String login, String password);

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
    User registerUser(String name, String phone, String login, String password, String inputIsAdmin, String adminPassword);

    /**
     * Метод авторизации пользователя
     *
     * @param login    Логин пользователя
     * @param password Пароль пользователя
     * @return User
     */
    User authorizeUser(String login, String password);

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User>
     */
    Set<User> getAllUsers();

    /**
     * Метод фиксирует выход пользователя из приложения
     *
     * @param user Пользователь
     */
    void recordExit(User user);
}
