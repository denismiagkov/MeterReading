package com.denmiagkov.meter.application.service;


import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.dto.incoming.UserDtoLogin;
import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;

import java.util.Set;

/**
 * Интерфейс, объявляющий логику обработки данных о пользователе
 */

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
    UserDto registerUser(UserRegisterDto userDto);

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User> Множество всех зарегистрированных пользователей
     */
    Set<UserDto> getAllUsers();

    /**
     * Метод фиксирует выход пользователя из приложения
     *
     * @param user Пользователь
     */
    void recordExit(UserDto userDto);

    /**
     * Метод возвращает пароль пользователя по его логину
     */
    UserDtoLogin getPasswordByLogin(String login);
}
