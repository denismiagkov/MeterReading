package com.denmiagkov.meter.infrastructure.in.servlet.public_servlet;

import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.servlet.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.infrastructure.in.validator.exception.IncorrectInputNameException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.Request;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServletTest {

    @Mock
    Logger log;
    @Mock
    ObjectMapper jsonMapper;
    @Mock
    IncomingDtoBuilder dtoBuilder;
    @Mock
    Controller controller;
    @InjectMocks
    RegistrationServlet servlet;
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
    @DisplayName("Method invokes appropriate methods on dependent objects (dtoBuilder, controller, jsonMapper)" +
                 "and successfully ends")
    void doPost_RightWork() throws IOException, ServletException {
        UserRegisterDto requestDto = mock(UserRegisterDto.class);
        when(dtoBuilder.createUserRegisterDto(inputStream)).
                thenReturn(requestDto);
        UserDto userDto = mock(UserDto.class);
        when(controller.registerUser(requestDto))
                .thenReturn(userDto);

        servlet.doPost(request, response);

        verify(dtoBuilder, times(1)).createUserRegisterDto(inputStream);
        verify(controller, times(1)).registerUser(requestDto);
        verify(jsonMapper, times(1)).writeValue(outputStream, userDto);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Method handles IncorrectInputNameException and set response status 400")
    void doPost_ihui() throws IOException, ServletException {
        when(dtoBuilder.createUserRegisterDto(inputStream))
                .thenThrow(IncorrectInputNameException.class);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}