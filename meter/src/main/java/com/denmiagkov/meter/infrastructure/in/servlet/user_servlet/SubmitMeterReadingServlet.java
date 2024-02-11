package com.denmiagkov.meter.infrastructure.in.servlet.user_servlet;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlet.public_servlet.RegistrationServlet;
import com.denmiagkov.meter.infrastructure.in.servlet.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.MeterReadingDtoValidatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Loggable
@WebServlet("/api/user/reading/new")
public class SubmitMeterReadingServlet extends HttpServlet {
    private ObjectMapper jsonMapper;
    private Controller controller;
    private AuthService authService;
    private IncomingDtoBuilder dtoBuilder;
    public static final Logger log = LoggerFactory.getLogger(RegistrationServlet.class);


    @Override
    public void init() throws ServletException {
        controller = Controller.INSTANCE;
        authService = AuthService.INSTANCE;
        jsonMapper = new ObjectMapper();
        jsonMapper.findAndRegisterModules();
        dtoBuilder = new IncomingDtoBuilder(jsonMapper, authService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try (InputStream inputStream = req.getInputStream();
             OutputStream outputStream = resp.getOutputStream()) {
            try {
                if (authService.validateAccessToken(token)) {
                    MeterReadingSubmitDto requestMeterReading = dtoBuilder.createNewMeterReadingSubmitDto(inputStream, token);
                    MeterReadingDto newMeterReading = controller.submitNewMeterReading(requestMeterReading);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    jsonMapper.writeValue(resp.getOutputStream(), newMeterReading);
                } else {
                    throw new AuthenticationFailedException();
                }
            } catch (AuthenticationFailedException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonMapper.writeValue(outputStream, e.getMessage());
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonMapper.writeValue(outputStream, e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}