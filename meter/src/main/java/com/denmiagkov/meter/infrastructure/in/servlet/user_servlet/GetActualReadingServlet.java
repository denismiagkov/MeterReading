package com.denmiagkov.meter.infrastructure.in.servlet.user_servlet;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.MeterReadingDtoValidatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/api/user/reading/actual")
public class GetActualReadingServlet extends HttpServlet {
    ObjectMapper mapper;
    Controller controller;
    AuthService authService;
    MeterReadingDtoValidatorImpl validator;

    @Override
    public void init() throws ServletException {
        controller = (Controller) this.getServletContext().getAttribute("controller");
        authService = (AuthService) this.getServletContext().getAttribute("authService");
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        validator = new MeterReadingDtoValidatorImpl(controller);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try {
            if (authService.validateAccessToken(token)) {
                MeterReadingDto requestMeterReading = createNewMeterReading(req, token);
                validator.isValidMeterReadingUtilityType(requestMeterReading);
                MeterReadingDto responseMeterReading =
                        controller.getActualReadingOnExactUtilityByUser(requestMeterReading);
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getOutputStream(), responseMeterReading);
            } else {
                throw new AuthenticationFailedException();
            }
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getOutputStream(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), e.getMessage());
        }
    }

    private MeterReadingDto createNewMeterReading(HttpServletRequest req, String token) throws IOException {
        MeterReadingDto meterReading = mapper.readValue(req.getInputStream(), MeterReadingDto.class);
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        return meterReading;
    }
}
