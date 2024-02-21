package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.LoginUserDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.dto.incoming.RegisterUserDto;
import com.denmiagkov.meter.application.service.exceptions.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.service.exceptions.UserAlreadyExistsException;

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
    UserDto registerUser(RegisterUserDto userDto);

    /**
     * Метод возвращает множество всех пользователей
     *
     * @return Set<User> Множество всех зарегистрированных пользователей
     */
    Set<UserDto> getAllUsers(Pageable pageable);

    /**
     * Метод возвращает пароль пользователя по его логину
     */
    LoginUserDto getPasswordByLogin(String login);
}
