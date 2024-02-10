package com.denmiagkov.meter.infrastructure.in.servlet.user_servlet;

import com.denmiagkov.meter.application.dto.MeterReadingSubmitDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Loggable
@WebServlet("/api/user/readings/month")
public class GetReadingsByMonthByUserServlet extends HttpServlet {
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
                Map<String, Integer> requestMonth = mapper.readValue(req.getInputStream(), HashMap.class);
                validator.isValidMonth(requestMonth);
                int userId = authService.getUserIdFromToken(token);
                List<MeterReadingSubmitDto> readingsForMonth =
                        controller.getReadingsForMonthByUser(userId, requestMonth);
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getOutputStream(), readingsForMonth);
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
}
