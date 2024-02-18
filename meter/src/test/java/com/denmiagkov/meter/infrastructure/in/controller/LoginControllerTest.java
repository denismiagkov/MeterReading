package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.mapper.UserMapper;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputNameException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.denmiagkov.meter.infrastructure.in.utils.IncomingDtoHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @Mock
    UserService userService;
    @Mock
    AuthService authService;
    @Mock
    IncomingDtoHandler dtoHandler;
    @InjectMocks
    LoginController loginController;
    ObjectMapper mapper = new ObjectMapper();
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    @DisplayName("Method receives post-request and receives user dto in json format")
    void registerUser() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto("Ivan", "+7112233", "Moscow", UserRole.USER,
                "user", "123", null);
        User user = new User(1, "Ivan", "+7112233", "Moscow", UserRole.USER,
                "user", "123");
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        String requestJson = mapper.writeValueAsString(registerDto);
        when(userService.registerUser(any(UserRegisterDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/registration")
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.name").value(registerDto.getName()),
                        jsonPath("$.phone").value(registerDto.getPhone()),
                        jsonPath("$.address").value(registerDto.getAddress()),
                        jsonPath("$.login").value(registerDto.getLogin())
                );
    }

    @Test
    @DisplayName("Method throws exception when user enters invalid data (incorrect name)")
    void registerUser_ThrowsException() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto("Ivan100", "+7112233", "Moscow", UserRole.USER,
                "user", "123", null);
        User user = new User(1, "Ivan", "+7112233", "Moscow", UserRole.USER,
                "user", "123");
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        String requestJson = mapper.writeValueAsString(registerDto);
//        when(dtoHandler.verifyUserRegisterDto(registerDto)
//                .thenThrow(IncorrectInputNameException.class);

        mockMvc.perform(post("/api/v1/registration")
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof IncorrectInputNameException)
                );
    }

    @Test
    @DisplayName("Method receives JwtRequest object (user credentials) in json format and returns JwtResponse (token) successfully")
    void login() throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin("user");
        jwtRequest.setPassword("123");
        String requestJson = mapper.writeValueAsString(jwtRequest);
        when(authService.login(any(JwtRequest.class))).thenReturn(any(JwtResponse.class));

        mockMvc.perform(post("/api/v1/login")
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}