package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.exception.AuthenticationFailedException;
import com.denmiagkov.meter.domain.User;

import java.util.Set;
/**
 * Интерфейс, объявляющий методы взаимодействия с базой данных по поводу сведений о пользователях
 * */
public interface UserRepository {

    /**
     * Метод добавления нового пользователя в базу данных
     *
     * @param user Новый пользователь
     * @return int id пользователя
     */
    int addUser(User user);

    /**
     * Метод проверки, содержатся ли сведения о пользователе в базе данных
     *
     * @param user Пользователь
     * @return boolean true - в случае, если пользователь зарегистрирован. в противном случае - false
     */
    boolean isExistUser(User user);

    /**
     * Метод проверки, имеется ли в базе данных запись с указанным логином
     *
     * @param login Логин пользователя
     * @return boolean true - в случае, если логин уже используется, в противном случае - false
     */
    boolean isExistLogin(String login);

    /**
     * Метод проверки, содержится ли в базе данных запись с указанными логином и паролем,
     * и определения пользователя, владеющего ими
     *
     * @param login    Введенный пользователем логин
     * @param password Введенный пользователем пароль
     * @return User Зарегистрированный пользователь с указанными логином и паролем
     * @throws AuthenticationFailedException в случае, если пользователь с указанными логином и паролем не зарегистрирован
     */
    User authenticateUser(String login, String password);

    /**
     * Метод получения сведений о всех пользователях из базы данных
     *
     * @return Set<User> Множество зарегистрованных пользователей
     */
    Set<User> getAllUsers();
}
