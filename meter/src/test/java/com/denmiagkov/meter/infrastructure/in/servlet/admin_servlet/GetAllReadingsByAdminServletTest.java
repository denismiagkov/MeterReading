package com.denmiagkov.meter.infrastructure.in.servlet.admin_servlet;

import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetAllReadingsByAdminServletTest {

    @Mock
    ObjectMapper mapper;
    @Mock
    Controller controller;
    @Mock
    AuthService authService;
    @InjectMocks
    GetAllActionsByAdminServlet servlet;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    final int PAGE_SIZE = 50;

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