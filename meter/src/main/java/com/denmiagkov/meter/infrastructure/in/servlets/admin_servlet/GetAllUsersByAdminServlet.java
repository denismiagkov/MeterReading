package com.denmiagkov.meter.infrastructure.in.servlets.admin_servlet;

import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.servlets.public_servlet.RegistrationServlet;
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
import java.util.Set;

@Loggable
@WebServlet("/api/admin/users")
public class GetAllUsersByAdminServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(GetAllUsersByAdminServlet.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";
    ObjectMapper mapper;
    Controller controller;
    AuthService authService;

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
                    Set<UserDto> allUsers = controller.getAllUsers();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    mapper.writeValue(responseOutputStream, allUsers);
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
            log.error(EXCEPTION_MESSAGE, e);
        }
    }
}