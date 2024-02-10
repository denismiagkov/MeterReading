package com.denmiagkov.meter.infrastructure.in.servlet.user_servlet;

import com.denmiagkov.meter.application.dto.MeterReadingSubmitDto;
import com.denmiagkov.meter.aspect.annotations.Audit;
import com.denmiagkov.meter.aspect.annotations.Loggable;
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
@Loggable
@WebServlet("/api/user/reading/new")
public class SubmitMeterReadingServlet extends HttpServlet {
    ObjectMapper mapper;
    Controller controller;
    AuthService authService;
    MeterReadingDtoValidatorImpl validator;

    @Override
    public void init() throws ServletException {
        controller = (Controller) this.getServletContext().getAttribute("controller");
        authService = (AuthService) this.getServletContext().getAttribute("authService");
        validator = new MeterReadingDtoValidatorImpl(controller);
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try {
            if (authService.validateAccessToken(token)) {
                MeterReadingSubmitDto meterReading = createNewMeterReading(req, token);
                validator.isValid(meterReading);
                controller.submitNewMeterReading(meterReading);
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getOutputStream(), meterReading);
            } else {
                throw new AuthenticationFailedException();
            }
        } catch (AuthenticationFailedException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getOutputStream(), e.getMessage());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), e.getMessage());
        }
    }

    private MeterReadingSubmitDto createNewMeterReading(HttpServletRequest req, String token) throws IOException {
        MeterReadingSubmitDto meterReading = mapper.readValue(req.getInputStream(), MeterReadingSubmitDto.class);
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        meterReading.setDate(LocalDateTime.now());
        return meterReading;
    }
}