package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User registerUser(String name, String phone, String address, String login, String password);

    User registerUser(String name, String phone, String login, String password, String inputIsAdmin, String adminPassword);

    User authorizeUser(String login, String password);

    Set<User> getAllUsers();

    List<Activity> getUserActivitiesList();
    void recordExit(User user);
}
