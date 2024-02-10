package com.denmiagkov.meter.infrastructure.in.servlet.admin_servlet;

import com.denmiagkov.meter.application.dto.UserActionDto;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.PublicUtilityValidatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/dictionary/new")
public class AddUtulityTypeToDictionaryServlet extends HttpServlet {
    ObjectMapper mapper;
    Controller controller;
    AuthService authService;
    PublicUtilityValidatorImpl validator;
    public static final String KEY = "utility";

    @Override
    public void init() throws ServletException {
        controller = (Controller) this.getServletContext().getAttribute("controller");
        authService = (AuthService) this.getServletContext().getAttribute("authService");
        mapper = new ObjectMapper();
        validator = new PublicUtilityValidatorImpl(controller);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try {
            if (authService.validateAccessToken(token) && authService.isAdmin(token)) {
                Map<String, String> newUtilityName = mapper.readValue(req.getInputStream(), HashMap.class);
                validator.isValid(newUtilityName.get(KEY));
                Map<Integer, String> dictionary = controller.addUtilityTypeToDictionary(newUtilityName.get(KEY));
                resp.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(resp.getOutputStream(), dictionary);
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
}
