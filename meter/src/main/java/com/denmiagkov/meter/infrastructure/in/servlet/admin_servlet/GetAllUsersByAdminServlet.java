package com.denmiagkov.meter.infrastructure.in.servlet.admin_servlet;

import com.denmiagkov.meter.application.dto.UserDto;
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

import java.io.IOException;
import java.util.Set;

@Loggable
@WebServlet("/api/admin/users")
public class GetAllUsersByAdminServlet extends HttpServlet {
    ObjectMapper mapper;
    Controller controller;
    AuthService authService;

    @Override
    public void init() throws ServletException {
        controller = (Controller) this.getServletContext().getAttribute("controller");
        authService = (AuthService) this.getServletContext().getAttribute("authService");
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try {
            if (authService.validateAccessToken(token) && authService.isAdmin(token)) {
                Set<UserDto> allUsers = controller.getAllUsers();
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getOutputStream(), allUsers);
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
