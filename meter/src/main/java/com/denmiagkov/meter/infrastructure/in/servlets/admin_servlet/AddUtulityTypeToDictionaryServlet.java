package com.denmiagkov.meter.infrastructure.in.servlets.admin_servlet;

import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.servlets.public_servlet.RegistrationServlet;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.PublicUtilityValidatorImpl;
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
import java.util.HashMap;
import java.util.Map;

@Loggable
@WebServlet("/api/admin/dictionary/new")
public class AddUtulityTypeToDictionaryServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AddUtulityTypeToDictionaryServlet.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";
    ObjectMapper jsonMapper;
    Controller controller;
    AuthService authService;
    PublicUtilityValidatorImpl validator;
    public static final String KEY = "utility";

    @Override
    public void init() throws ServletException {
        controller = Controller.INSTANCE;
        authService = AuthService.INSTANCE;
        jsonMapper = new ObjectMapper();
        validator = PublicUtilityValidatorImpl.INSTANCE;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try (InputStream inputStream = req.getInputStream();
             OutputStream outputStream = resp.getOutputStream()) {
            try {
                if (authService.validateAccessToken(token) && authService.isAdmin(token)) {
                    Map<String, String> newUtilityName = jsonMapper.readValue(inputStream, HashMap.class);
                    validator.isValid(newUtilityName.get(KEY));
                    Map<Integer, String> dictionary = controller.addUtilityTypeToDictionary(newUtilityName.get(KEY));
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    jsonMapper.writeValue(outputStream, dictionary);
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
