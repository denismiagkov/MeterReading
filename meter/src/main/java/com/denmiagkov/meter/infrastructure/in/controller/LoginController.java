package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.denmiagkov.meter.infrastructure.in.utils.IncomingDtoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Контроллер регистрации и входа в приложение
 */
@RestController
@RequestMapping("/api/v1")
public class LoginController {
    /**
     * Сервис пользователя
     */
    private final UserService userService;
    private final AuthService authService;
    private final IncomingDtoHandler dtoHandler;

    /**
     * Конструктор
     */
    @Autowired
    public LoginController(UserService userService,
                           AuthService authService,
                           IncomingDtoHandler dtoHandler) {
        this.userService = userService;
        this.authService = authService;
        this.dtoHandler = dtoHandler;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello From Spring");
    }


    /**
     * Метод регистрации пользователя
     *
     * @param userRegisterDto Входящее ДТО регистрации пользователя
     * @return User Зарегистрированный пользователь
     */

    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        dtoHandler.verifyUserRegisterDto(userRegisterDto);
        return userService.registerUser(userRegisterDto);
    }

    /**
     * Метод входа пользователя в приложение
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }
}
