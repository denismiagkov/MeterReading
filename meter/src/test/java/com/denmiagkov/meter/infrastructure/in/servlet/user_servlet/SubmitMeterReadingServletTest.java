package com.denmiagkov.meter.infrastructure.in.servlet.user_servlet;

import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlet.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.infrastructure.in.validator.exception.IncorrectInputNameException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmitMeterReadingServletTest {
    @Mock
    Logger log;
    @Mock
    ObjectMapper jsonMapper;
    @Mock
    AuthService authService;
    @Mock
    IncomingDtoBuilder dtoBuilder;
    @Mock
    Controller controller;
    @InjectMocks
    GetAllActualReadingsByUserServlet servlet;
    HttpServletRequest request;
    HttpServletResponse response;
    InputStream inputStream;
    OutputStream outputStream;


    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        inputStream = request.getInputStream();
        outputStream = response.getOutputStream();
    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object authService")
    void doPost_AuthServiceReturnsToken() throws IOException, ServletException {
        servlet.doPost(request, response);

        verify(authService, times(1)).getTokenFromRequest(request);
    }

    @Test
    @DisplayName("Method invokes appropriate methods on dependent objects (dtoBuilder, controller, jsonMapper)" +
                 "and successfully ends")
    void doPost_RightWork() throws IOException, ServletException {
        String token = "dummy";
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(true);

        servlet.doPost(request, response);

        verify(authService, times(1)).getTokenFromRequest(request);
        verify(authService, times(1)).validateAccessToken(token);
    }

    @Test
    @DisplayName("Method handles IncorrectInputNameException and set response status 400")
    void doPost_ihui() throws IOException, ServletException {
        String token = "dummy";
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(true);
        when(dtoBuilder.createUserRegisterDto(inputStream))
                .thenThrow(IncorrectInputNameException.class);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}