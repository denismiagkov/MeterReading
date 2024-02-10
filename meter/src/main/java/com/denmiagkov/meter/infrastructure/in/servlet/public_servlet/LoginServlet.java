package com.denmiagkov.meter.infrastructure.in.servlet.public_servlet;

import com.denmiagkov.meter.application.repository.ActivityRepositoryImpl;
import com.denmiagkov.meter.application.repository.UserRepositoryImpl;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserActivityServiceImpl;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtProvider;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private ObjectMapper mapper;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        UserActivityService activityService = new UserActivityServiceImpl(new ActivityRepositoryImpl());
        UserService userService = new UserServiceImpl(new UserRepositoryImpl(), activityService);
        authService = new AuthService(userService, new JwtProvider(userService));
        mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            JwtRequest jwtRequest = mapper.readValue(req.getInputStream(), JwtRequest.class);
            JwtResponse jwtResponse = authService.login(jwtRequest);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setCharacterEncoding("UTF-8");
            resp.getOutputStream().write(mapper.writeValueAsBytes(jwtResponse));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), e.getMessage());
        }
    }
}
