package com.denmiagkov.meter.infrastructure.in.servlet.public_servlet;

import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.dto.UserIncomingDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.UserIncomingDtoValidatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Loggable
@WebServlet("/api/registration")
public class RegistrationServlet extends HttpServlet {

    private ObjectMapper mapper;
    private UserIncomingDtoValidatorImpl validator;
    private transient Controller controller;

    @Override
    public void init() throws ServletException {
        validator = new UserIncomingDtoValidatorImpl();
        controller = (Controller) this.getServletContext().getAttribute("controller");
        mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("entered in doPost");
        resp.setContentType("application/json");
        try (InputStream requestInputStream = req.getInputStream();
             OutputStream responseOutputStream = resp.getOutputStream()) {
            try {
                UserIncomingDto userInDto = mapper.readValue(requestInputStream, UserIncomingDto.class);
                validator.isValid(userInDto);
                UserDto userOutDto = controller.registerUser(userInDto);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                mapper.writeValue(responseOutputStream, userOutDto);
            } catch (RuntimeException | IOException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(responseOutputStream, e.getMessage());
            }
        }
    }
}

