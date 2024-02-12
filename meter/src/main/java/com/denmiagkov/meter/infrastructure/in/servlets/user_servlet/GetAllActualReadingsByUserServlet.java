package com.denmiagkov.meter.infrastructure.in.servlets.user_servlet;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewActualDto;
import com.denmiagkov.meter.aspect.annotations.Loggable;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.servlets.utils.IncomingDtoBuilder;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
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
@WebServlet("/api/user/readings/actual")
public class GetAllActualReadingsByUserServlet extends HttpServlet {
    public static final Logger log = LoggerFactory.getLogger(GetAllActualReadingsByUserServlet.class);
    ObjectMapper jsonMapper;
    Controller controller;
    AuthService authService;
    IncomingDtoBuilder dtoBuilder;

    @Override
    public void init() throws ServletException {
        controller = Controller.INSTANCE;
        authService = AuthService.INSTANCE;
        jsonMapper = new ObjectMapper();
        jsonMapper.findAndRegisterModules();
        dtoBuilder = new IncomingDtoBuilder(jsonMapper, authService);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String token = authService.getTokenFromRequest(req);
        try (OutputStream outputStream = resp.getOutputStream()) {
            try {
                if (authService.validateAccessToken(token)) {
                    MeterReadingReviewActualDto requestDto = dtoBuilder.createMeterReadingReviewAllActualsDto(token);
                    List<MeterReadingDto> allActualReadings =
                            controller.getActualMeterReadingsOnAllUtilitiesByUser(requestDto);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    jsonMapper.writeValue(outputStream, allActualReadings);
                } else {
                    throw new AuthenticationFailedException();
                }
            } catch (AuthenticationFailedException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonMapper.writeValue(outputStream, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonMapper.writeValue(outputStream, e.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
