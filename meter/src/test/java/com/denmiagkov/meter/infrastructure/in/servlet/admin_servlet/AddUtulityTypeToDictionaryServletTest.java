package com.denmiagkov.meter.infrastructure.in.servlet.admin_servlet;

import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.PublicUtilityValidatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddUtulityTypeToDictionaryServletTest {

    @Mock
    ObjectMapper mapper;
    @Mock
    Controller controller;
    @Mock
    AuthService authService;
    @Mock
    PublicUtilityValidatorImpl validator;
    @InjectMocks
    AddUtulityTypeToDictionaryServlet servlet;
    public static final String KEY = "utility";
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Method invokes all appropriate methods of dependent objects and ends successfully")
    void doPost() throws IOException {
        String token = "dummy";
        Map<String, String> newUtilityName = mock(HashMap.class);
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(true);
        when(authService.isAdmin(token)).thenReturn(true);
        when(mapper.readValue(request.getInputStream(), HashMap.class))
                .thenReturn((HashMap) newUtilityName);
        when(validator.isValid(newUtilityName.get(KEY))).thenReturn(true);
        Map<Integer, String> dictionary = mock(HashMap.class);
        when(controller.addUtilityTypeToDictionary(newUtilityName.get(KEY)))
                .thenReturn(dictionary);

        servlet.doPost(request, response);

        verify(authService, times(1)).getTokenFromRequest(request);
        verify(authService, times(1)).validateAccessToken(token);
        verify(authService, times(1)).isAdmin(token);
        verify(mapper, times(1)).readValue(request.getInputStream(), HashMap.class);
        verify(validator, times(1)).isValid(newUtilityName.get(KEY));
        verify(controller).addUtilityTypeToDictionary(newUtilityName.get(KEY));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(mapper).writeValue(response.getOutputStream(), dictionary);
    }

    @Test
    @DisplayName("Method handles AuthenticationFailedException when user entered incorrect password")
    void doPost_ThrowsExceptionWhenInvalidPassword() throws IOException {
        String token = "dummy";
        String exceptionMessage = "Ошибка авторизации: пользователя с указанными логином и паролем не существует!";
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(true);
        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mapper).writeValue(response.getOutputStream(), exceptionMessage);
    }

    @Test
    @DisplayName("Method handles AuthenticationFailedException when user is not admin")
    void doPost_ThrowsExceptionWhenIsNotAdmin() throws IOException {
        String token = "dummy";
        String exceptionMessage = "Ошибка авторизации: пользователя с указанными логином и паролем не существует!";
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(true);
        when(authService.isAdmin(token)).thenReturn(false);
        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mapper).writeValue(response.getOutputStream(), exceptionMessage);
    }
}