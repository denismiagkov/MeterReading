package com.denmiagkov.meter.infrastructure.in.servlet.user_servlet;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewHistoryDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlet.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Loggable
@WebServlet("/api/user/readings")
public class GetReadingHistoryByUserServlet extends HttpServlet {
    ObjectMapper jsonMapper;
    Controller controller;
    AuthService authService;
    IncomingDtoBuilder dtoBuilder;
    private static final int PAGE_SIZE = 50;

    @Override
    public void init() throws ServletException {
        controller = Controller.INSTANCE;
        authService = AuthService.INSTANCE;
        jsonMapper = new ObjectMapper();
        jsonMapper.findAndRegisterModules();
        dtoBuilder = new IncomingDtoBuilder(jsonMapper, authService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try {
            if (authService.validateAccessToken(token)) {
                MeterReadingReviewHistoryDto requestDto = dtoBuilder.createMeterReadingReviewHistoryDto(token);
                List<List<MeterReadingDto>> readingsHistory =
                        controller.getMeterReadingsHistoryByUser(requestDto, PAGE_SIZE);
                resp.setStatus(HttpServletResponse.SC_OK);
                jsonMapper.writeValue(resp.getOutputStream(), readingsHistory);
            } else {
                throw new AuthenticationFailedException();
            }
        } catch (AuthenticationFailedException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonMapper.writeValue(resp.getOutputStream(), e.getMessage());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMapper.writeValue(resp.getOutputStream(), e.getMessage());
        }
    }
}
