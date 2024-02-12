package com.denmiagkov.meter.infrastructure.in.servlets.user_servlet;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewForMonthDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlets.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Loggable
@WebServlet("/api/user/readings/month")
public class GetReadingsByMonthByUserServlet extends HttpServlet {
    ObjectMapper jsonMapper;
    Controller controller;
    AuthService authService;
    IncomingDtoBuilder dtoBuilder;

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
                MeterReadingReviewForMonthDto requestDto = dtoBuilder.createMeterReadingReviewForMonthDto(req, token);
                List<MeterReadingDto> readingsForMonth =
                        controller.getReadingsForMonthByUser(requestDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                jsonMapper.writeValue(resp.getOutputStream(), readingsForMonth);
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
