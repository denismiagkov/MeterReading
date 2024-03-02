package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
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
     * @param userDto Входящее ДТО, содержащее регистрационные данные пользователя
     * @return UserDto Исходящее ДТО со сведениями о зарегистрированом пользователе
     * @throws LoginAlreadyInUseException при использовании логина, уже зарегистрированного в системе
     * @throws UserAlreadyExistsException при попытке повторной регистрации одного и того же пользователя
     */
    UserDto registerUser(RegisterUserDto userDto);

    /**
     * Метод возвращает множество всех пользователей
     *
     * @param pageable Параметры пагинации
     * @return Set<UserDto> Множество всех зарегистрированных пользователей
     */
    Set<UserDto> getAllUsers(Pageable pageable);

    /**
     * Метод возвращает пароль пользователя по его логину
     *
     * @param login логин пользователя
     * @return UserLoginDto Сведения о пользователе
     */
    UserLoginDto getPasswordByLogin(String login);
}
