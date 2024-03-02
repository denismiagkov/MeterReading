package com.denmiagkov.meter.IT;

import com.denmiagkov.meter.application.dto.incoming.RegisterUserDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.mapper.UserRegisterMapper;
import com.denmiagkov.meter.IT.config.PostgresExtension;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.application.service.exceptions.AuthenticationFailedException;
import com.denmiagkov.meter.application.service.exceptions.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.service.exceptions.UserAlreadyExistsException;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputNameException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputPasswordException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputPhoneNumberException;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgresExtension.class)
@DirtiesContext
class LoginControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserRegisterMapper userRegisterMapper;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("User is successfully registered and saved in database")
    void registerUser_Successful() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Oleg")
                .phone("+71234567890")
                .address("Moscow")
                .login("user100")
                .password("secret123")
                .build();
        String jsonRequest = mapper.writeValueAsString(registerUserDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/registration")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isCreated()
                ).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        UserDto resultUserDto = mapper.readValue(responseBody, UserDto.class);
        User user = userRegisterMapper.incomingUserDtoToUser(registerUserDto);

        assertAll(
                () -> assertThat(registerUserDto.getName()).isEqualTo(resultUserDto.getName()),
                () -> assertThat(registerUserDto.getPhone()).isEqualTo(resultUserDto.getPhone()),
                () -> assertThat(registerUserDto.getAddress()).isEqualTo(resultUserDto.getAddress()),
                () -> assertThat(registerUserDto.getLogin()).isEqualTo(resultUserDto.getLogin()),
                () -> assertThat(resultUserDto.getRole()).isEqualTo(UserRole.USER),
                () -> assertThat(resultUserDto.getId()).isPositive()
        );
        assertThat(repository.isExistUser(user)).isTrue();
    }

    @Test
    @DisplayName("User is already registered and method throws exception")
    void registerUser_ThrowsUserAlreadyExistsException() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Alex")
                .phone("+70987654321")
                .address("Moscow")
                .login("user100")
                .password("secret123")
                .build();
        String jsonRequest = mapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/api/v1/registration")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException)
                );
    }

    @Test
    @DisplayName("User enters login that already is used and method throws exception")
    void registerUser_ThrowsLoginAlreadyInUseException() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Oleg")
                .phone("+71234567890")
                .address("Moscow")
                .login("user1")
                .password("secret123")
                .build();
        String jsonRequest = mapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/api/v1/registration")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof LoginAlreadyInUseException)
                );
    }

    @Test
    @DisplayName("User enters incorrect password (length less than 8 symbols) and method throws exception")
    void registerUser_ThrowsIncorrectInputPasswordException() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Oleg")
                .phone("+71234567890")
                .address("Moscow")
                .login("testuser")
                .password("secret")
                .build();
        String jsonRequest = mapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/api/v1/registration")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof IncorrectInputPasswordException)
                );
    }

    @Test
    @DisplayName("User enters incorrect name (contains digit) and method throws exception")
    void registerUser_ThrowsIncorrectInputNameException() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Oleg2")
                .phone("+71234567890")
                .address("Moscow")
                .login("testuser")
                .password("secret123")
                .build();
        String jsonRequest = mapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/api/v1/registration")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof IncorrectInputNameException)
                );
    }

    @Test
    @DisplayName("User enters incorrect email (contains non-digit) and method throws exception")
    void registerUser_ThrowsIncorrectInputPhoneNumberException() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Oleg")
                .phone("+7123456_7890")
                .address("Moscow")
                .login("testuser")
                .password("secret123")
                .build();
        String jsonRequest = mapper.writeValueAsString(registerUserDto);

        mockMvc.perform(post("/api/v1/registration")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof IncorrectInputPhoneNumberException)
                );
    }

    @Test
    @DisplayName("After registration user enters right login and password and authentication is successful")
    void login_Successful() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Petr")
                .phone("+71112223344")
                .address("Moscow")
                .login("user")
                .password("secret456")
                .build();
        String registerRequest = mapper.writeValueAsString(registerUserDto);
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setLogin("user");
        loginRequest.setPassword("secret456");
        String jsonRequest = mapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/v1/registration")
                .content(registerRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/login")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                ).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        JwtResponse token = mapper.readValue(responseBody, JwtResponse.class);

        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("After registration user enters wrong password and authentication is failed")
    void login_ThrowsAuthenticationFailedException() throws Exception {
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .name("Petr")
                .phone("+71112223344")
                .address("Moscow")
                .login("user")
                .password("secret456")
                .build();
        String registerRequest = mapper.writeValueAsString(registerUserDto);
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setLogin("user");
        loginRequest.setPassword("dummy");
        String jsonRequest = mapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/v1/registration")
                .content(registerRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));
        mockMvc.perform(post("/api/v1/login")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isUnauthorized(),
                        result -> assertTrue(result.getResolvedException() instanceof AuthenticationFailedException)
                );
    }
}