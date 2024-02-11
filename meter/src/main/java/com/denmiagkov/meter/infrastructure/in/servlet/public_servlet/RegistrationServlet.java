package com.denmiagkov.meter.infrastructure.in.servlet.public_servlet;

import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.servlet.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.UserIncomingDtoValidatorImpl;
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
@WebServlet("/api/registration")
public class RegistrationServlet extends HttpServlet {
    public static final Logger log = LoggerFactory.getLogger(RegistrationServlet.class);

    private ObjectMapper jsonMapper;
    private transient IncomingDtoBuilder dtoBuilder;
    private transient Controller controller;

    @Override
    public void init() throws ServletException {
        controller = Controller.INSTANCE;
        jsonMapper = new ObjectMapper();
        dtoBuilder = new IncomingDtoBuilder(jsonMapper);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (InputStream inputStream = req.getInputStream();
             OutputStream outputStream = resp.getOutputStream()) {
            try {
                UserRegisterDto userIncomingDto = dtoBuilder.createUserRegisterDto(inputStream);
                UserDto userDto = controller.registerUser(userIncomingDto);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                jsonMapper.writeValue(outputStream, userDto);
            } catch (RuntimeException | IOException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonMapper.writeValue(outputStream, e.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

