package com.denmiagkov.meter.infrastructure.in.servlets.public_servlet;

import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
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
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";

    private ObjectMapper jsonMapper;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = AuthService.INSTANCE;
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (InputStream requestInputStream = req.getInputStream();
             OutputStream responseOutputStream = resp.getOutputStream()) {
            try {
                JwtRequest jwtRequest = jsonMapper.readValue(requestInputStream, JwtRequest.class);
                JwtResponse jwtResponse = authService.login(jwtRequest);
                resp.setStatus(HttpServletResponse.SC_OK);
                responseOutputStream.write(jsonMapper.writeValueAsBytes(jwtResponse));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonMapper.writeValue(responseOutputStream, e.getMessage());
            }
        } catch (Exception e) {
            log.error(EXCEPTION_MESSAGE, e);
        }
    }
}
