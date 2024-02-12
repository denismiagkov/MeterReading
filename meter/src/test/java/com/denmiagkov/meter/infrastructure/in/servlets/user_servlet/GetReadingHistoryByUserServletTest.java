package com.denmiagkov.meter.infrastructure.in.servlets.user_servlet;

import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewHistoryDto;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlets.utils.IncomingDtoBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetReadingHistoryByUserServletTest {

    @Mock
    ObjectMapper jsonMapper;
    @Mock
    AuthService authService;
    @Mock
    IncomingDtoBuilder dtoBuilder;
    @Mock
    Controller controller;
    @InjectMocks
    GetReadingHistoryByUserServlet servlet;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    private static final int PAGE_SIZE = 50;

    @Test
    @DisplayName("Method invokes all appropriate methods of dependent objects and ends successfully")
    void doPost_RightWork() throws IOException {
        String token = "dummy";
        MeterReadingReviewHistoryDto requestDto = mock(MeterReadingReviewHistoryDto.class);
        List<List<MeterReadingDto>> readingsHistory = mock(ArrayList.class);
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(true);
        when(dtoBuilder.createMeterReadingReviewHistoryDto(token)).thenReturn(requestDto);
        when(controller.getMeterReadingsHistoryByUser(requestDto, PAGE_SIZE))
                .thenReturn(readingsHistory);

        servlet.doPost(request, response);

        verify(authService, times(1)).getTokenFromRequest(request);
        verify(authService, times(1)).validateAccessToken(token);
        verify(dtoBuilder, times(1)).createMeterReadingReviewHistoryDto(token);
        verify(controller, times(1)).getMeterReadingsHistoryByUser(requestDto, PAGE_SIZE);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(jsonMapper).writeValue(response.getOutputStream(), readingsHistory);
    }

    @Test
    @DisplayName("Method handles AuthenticationFailedException when user entered incorrect password")
    void doPost_ThrowsException() throws IOException {
        String token = "dummy";
        String exceptionMessage = "Ошибка авторизации: пользователя с указанными логином и паролем не существует!";
        when(authService.getTokenFromRequest(request)).thenReturn(token);
        when(authService.validateAccessToken(token)).thenReturn(false);

        servlet.doPost(request, response);
        
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(jsonMapper).writeValue(response.getOutputStream(), exceptionMessage);
    }
}