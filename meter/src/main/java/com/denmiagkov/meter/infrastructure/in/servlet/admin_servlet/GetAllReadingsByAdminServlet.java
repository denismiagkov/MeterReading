package com.denmiagkov.meter.infrastructure.in.servlet.admin_servlet;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Loggable
@WebServlet("/api/admin/readings")
public class GetAllReadingsByAdminServlet extends HttpServlet {
    public static final Logger log = LoggerFactory.getLogger(GetAllReadingsByAdminServlet.class);
    ObjectMapper mapper;
    Controller controller;
    AuthService authService;
    private static final int PAGE_SIZE = 50;

    @Override
    public void init() throws ServletException {
        controller = Controller.INSTANCE;
        authService = AuthService.INSTANCE;
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try (OutputStream responseOutputStream = resp.getOutputStream()) {
            try {
                if (authService.validateAccessToken(token) && authService.isAdmin(token)) {
                    List<List<MeterReadingDto>> allMeterReadings = controller.getAllReadingsList(PAGE_SIZE);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    mapper.writeValue(responseOutputStream, allMeterReadings);
                } else {
                    throw new AuthenticationFailedException();
                }
            } catch (AuthenticationFailedException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                mapper.writeValue(responseOutputStream, e.getMessage());
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(responseOutputStream, e.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}