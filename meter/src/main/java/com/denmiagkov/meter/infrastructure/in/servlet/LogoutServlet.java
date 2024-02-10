package com.denmiagkov.meter.infrastructure.in.servlet;

import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

//@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet {

    private ObjectMapper mapper;
    private transient Controller controller;

    @Override
    public void init() throws ServletException {
        controller = (Controller) this.getServletContext().getAttribute("controller");
        mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (OutputStream responseOutputStream = resp.getOutputStream()) {
            try {
                UserDto userDto = (UserDto) req.getSession().getAttribute("user");
                req.getSession().invalidate();
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(responseOutputStream, userDto);
            } catch (RuntimeException | IOException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                mapper.writeValue(responseOutputStream, e.getMessage());
            }
        }
    }
}
