package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.exception.AuthenticationFailedException;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.Reading;
import com.denmiagkov.meter.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Класс, отвечающий за хранение данных в памяти приложения
 */
@Getter
@NoArgsConstructor
public class Storage {
    /**
     * Множество всех пользователей
     */
    private static final Set<User> USERS = new HashSet<>();
    /**
     * Список всех переданных показаний
     */
    private static final List<Reading> READINGS = new ArrayList<>();
    /**
     * Список всех действий, совершенных пользователями в системе
     */
    private static final List<Activity> ACTIVITIES = new ArrayList<>();

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
     * Метод добавления нового пользовательского действия в колекцию
     *
     * @param activity Новое действие пользователя
     * @return boolean true - в случае успешного добавления,в противном случае - false
     */
    public boolean addActivity(Activity activity) {
        return ACTIVITIES.add(activity);
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
        String message = "Ошибка авторизации: пользователя с указанными логином и паролем не существует!";
        throw new AuthenticationFailedException(message);
    }

    /**
     * Метод добавления новых показаний в коллекцию
     *
     * @param reading новые показания счетчика
     */
    public void addNewReading(Reading reading) {
        READINGS.add(reading);
    }

    /**
     * Метод просмотра актуальных (последних переданных) показаний счетчиков
     *
     * @param user Пользователь
     * @return Reading последние переданные показания счетчиков
     */
    public Reading getLastReading(User user) {
        return READINGS.stream()
                .filter(e -> e.getUserId().equals(user.getId()))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    /**
     * Метод получения множества всех зарегистрированных пользователей
     *
     * @return Set<User> Множество зарегистрованных пользователей
     */
    public Set<User> getUsers() {
        return USERS;
    }

    /**
     * Метод получения всех переданных показаний
     *
     * @return List<Reading> Список всех переданных показаний
     */
    public List<Reading> getAllReadingsList() {
        return READINGS;
    }

    /**
     * Метод получения всех действий, совершенных пользователями в системе
     *
     * @return List<Activity> Список действий пользователей
     */
    public List<Activity> getActivitiesList() {
        return ACTIVITIES;
    }

    /**
     * Метод получения истории подачи показаний конкретным пользователем
     *
     * @param user Пользователь
     * @return List<Reading> Список показаний, поданных указанным пользователем
     */
    public List<Reading> getReadingsHistory(User user) {
        return READINGS.stream()
                .filter(e -> e.getUserId().equals(user.getId()))
                .toList();
    }

    /**
     * Метод получения показаний, переданных указанным пользователем, в определенный месяц
     *
     * @param user  Пользователь
     * @param year  Год
     * @param month Месяц
     * @return Reading Показания счетчика пользователя за указанные год и месяц
     */
    public Reading getReadingsForMonthByUser(User user, int year, int month) {
        return READINGS.stream()
                .filter(e ->
                        ((e.getDate().getYear() == year) && (e.getDate().getMonthValue() == month)))
                .findFirst()
                .orElse(null);
    }
}