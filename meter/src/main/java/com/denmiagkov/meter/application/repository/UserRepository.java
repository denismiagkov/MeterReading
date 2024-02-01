package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.exception.AuthenticationFailedException;
import com.denmiagkov.meter.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс, отвечающий за хранение данных о пользователях в памяти приложения
 */
@Getter
@NoArgsConstructor
public class UserRepository {
    /**
     * Множество всех пользователей
     */
    private static final Set<User> USERS = new HashSet<>();

    /**
     * Метод добавления нового пользователя в коллекцию
     *
     * @param user Новый пользователь
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    public boolean addUser(User user) {
        return USERS.add(user);
    }

    /**
     * Метод проверки, зарегистрирован ли указанный пользователь в системе
     *
     * @param user Пользователь
     * @return boolean true - в случае, если пользователь зарегистрирован. в противном случае - false
     */
    public boolean isExistUser(User user) {
        return USERS.contains(user);
    }

    /**
     * Метод проверки, используется ли указанный логин каким-либо пользователем
     *
     * @param login Логин пользователя
     * @return boolean true - в случае, если логинуже используется, в противном случае - false
     */
    public boolean isExistLogin(String login) {
        return USERS.stream()
                .anyMatch(e -> e.getLogin().equals(login));
    }

    /**
     * Метод аутентификации пользователя путем сопоставления преданных логина и пароля с данными в коллекции
     *
     * @param login    Введенный пользователем логин
     * @param password Введенный пользователем пароль
     * @return User Зарегистрированный пользователь с указанными логином и паролем
     * @throws AuthenticationFailedException в случае, если пользователь с указанными логином и паролем не зарегистрирован
     */
    public User authorizeUser(String login, String password) {
        for (User user : USERS) {
            if (user.getLogin().equals(login)
                && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new AuthenticationFailedException();
    }

    /**
     * Метод получения множества всех зарегистрированных пользователей
     *
     * @return Set<User> Множество зарегистрованных пользователей
     */
    public Set<User> getUsers() {
        return USERS;
    }
}
