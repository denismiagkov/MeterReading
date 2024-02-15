package com.denmiagkov.meter.infrastructure.in.servlets.user_servlet;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlets.public_servlet.RegistrationServlet;
import com.denmiagkov.meter.infrastructure.in.servlets.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
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

@Loggable
@WebServlet("/api/user/reading/new")
public class SubmitMeterReadingServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(SubmitMeterReadingServlet.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";
    private ObjectMapper jsonMapper;
    private Controller controller;
    private AuthService authService;
    private IncomingDtoBuilder dtoBuilder;

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
            log.error(EXCEPTION_MESSAGE, e);
        }
    }
}