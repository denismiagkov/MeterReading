package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.denmiagkov.meter.infrastructure.in.dto_handling.IncomingDtoBuilder;
import com.denmiagkov.starter.logging.aspect.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер регистрации и входа в приложение
 */
@Tag(name = "Login")
@Loggable
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class LoginController {

    private final UserService userService;
    private final AuthService authService;
    private final IncomingDtoBuilder dtoHandler;

    /**
     * Метод регистрации пользователя
     *
     * @param registerUserDto Входящее ДТО регистрации пользователя
     * @return User Зарегистрированный пользователь
     */
    @Operation(
            summary = "Registration of new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created - User successfully registered",
                            content = @Content(
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BadRequest - User already has been registered " +
                                          "or there are mistakes in input data")
            })
    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Parameter(description = "user registration data") RegisterUserDto registerUserDto) {
        dtoHandler.verifyRegisterUserDto(registerUserDto);
        UserDto newUser = userService.registerUser(registerUserDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newUser);
    }

    /**
     * Метод входа пользователя в приложение
     */
    @Operation(
            summary = "User login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "ОК - Login successful",
                            content = @Content(
                                    schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User entered invalid login or password")
            })
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Parameter(description = "User login and password") JwtRequest jwtRequest) {
        JwtResponse accessToken = authService.login(jwtRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accessToken);
    }
}
