package com.denmiagkov.meter.infrastructure.in.servlets.utils;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.MeterReadingDtoValidatorImpl;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.UserIncomingDtoValidatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

public class IncomingDtoBuilder {
    private final ObjectMapper jsonMapper;
    private AuthService authService;
    private final MeterReadingDtoValidatorImpl meterReadingValidator;
    private final UserIncomingDtoValidatorImpl userIncomingDtoValidator;

    public IncomingDtoBuilder(ObjectMapper mapper, AuthService service) {
        this(mapper);
        authService = service;
    }

    public IncomingDtoBuilder(ObjectMapper mapper) {
        jsonMapper = mapper;
        meterReadingValidator = MeterReadingDtoValidatorImpl.INSTANCE;
        userIncomingDtoValidator = UserIncomingDtoValidatorImpl.INSTANCE;
    }

    public MeterReadingSubmitDto createNewMeterReadingSubmitDto(InputStream inputStream, String token) throws IOException {
        MeterReadingSubmitDto meterReading = jsonMapper.readValue(inputStream, MeterReadingSubmitDto.class);
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        meterReading.setDate(LocalDateTime.now());
        meterReadingValidator.isValid(meterReading);
        return meterReading;
    }

    public MeterReadingReviewForMonthDto createMeterReadingReviewForMonthDto(HttpServletRequest req, String token) throws IOException {
        MeterReadingReviewForMonthDto meterReading =
                jsonMapper.readValue(req.getInputStream(), MeterReadingReviewForMonthDto.class);
        meterReadingValidator.isValidMonth(meterReading);
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        return meterReading;
    }

    public MeterReadingReviewHistoryDto createMeterReadingReviewHistoryDto(String token) throws IOException {
        MeterReadingReviewHistoryDto meterReading = new MeterReadingReviewHistoryDto();
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        return meterReading;
    }

    public MeterReadingReviewActualDto createMeterReadingReviewAllActualsDto(String token) throws IOException {
        MeterReadingReviewActualDto meterReading = new MeterReadingReviewActualDto();
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        return meterReading;
    }

    public MeterReadingReviewActualDto createMeterReadingReviewOnConcreteUtilityDto(HttpServletRequest req, String token) throws IOException {
        MeterReadingReviewActualDto meterReading = jsonMapper.readValue(req.getInputStream(), MeterReadingReviewActualDto.class);
        meterReadingValidator.isValidMeterReadingUtilityType(meterReading);
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        return meterReading;
    }

    public UserRegisterDto createUserRegisterDto(InputStream inputStream) throws IOException {
        UserRegisterDto userIncomingDto = jsonMapper.readValue(inputStream, UserRegisterDto.class);
        userIncomingDtoValidator.isValid(userIncomingDto);
        return userIncomingDto;
    }
}
