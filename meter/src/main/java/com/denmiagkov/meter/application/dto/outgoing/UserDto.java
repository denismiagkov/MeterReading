package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.meter.domain.UserRole;
import lombok.*;

/**
 * Исходящее ДТО пользователя
 */
@NoArgsConstructor
@Data
public class UserDto {

    /**
     * id пользователя
     */
    private int id;

    /**
     * Имя пользователя
     */
    private String name;

    /**
     * Телефон пользователя
     */
    private String phone;

    /**
     * Адрес пользователя
     */
    private String address;

    /**
     * Роль пользователя
     */
    private UserRole role;

    /**
     * Логин пользователя
     */
    private String login;
}
