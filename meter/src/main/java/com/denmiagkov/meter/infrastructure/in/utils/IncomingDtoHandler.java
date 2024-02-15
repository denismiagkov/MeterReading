package com.denmiagkov.meter.infrastructure.in.utils;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.MeterReadingDtoValidatorImpl;
import com.denmiagkov.meter.infrastructure.in.validator.validatorImpl.UserIncomingDtoValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class IncomingDtoHandler {
    private final AuthService authService;
    private final MeterReadingDtoValidatorImpl meterReadingValidator;
    private final UserIncomingDtoValidatorImpl userIncomingDtoValidator;


    @Autowired
    public IncomingDtoHandler(MeterReadingDtoValidatorImpl meterReadingDtoValidator,
                              UserIncomingDtoValidatorImpl userIncomingDtoValidator,
                              AuthService authService) {
        this.meterReadingValidator = meterReadingDtoValidator;
        this.userIncomingDtoValidator = userIncomingDtoValidator;
        this.authService = authService;
    }

    public void updateNewMeterReadingSubmitDto(MeterReadingSubmitDto meterReading, String token) {
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        meterReading.setDate(LocalDateTime.now());
        meterReadingValidator.isValid(meterReading);
    }

    public void updateMeterReadingReviewForMonthDto(MeterReadingReviewForMonthDto requestDto, String token) {
        meterReadingValidator.isValidMonth(requestDto);
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
    }

    public void createMeterReadingReviewHistoryDto(MeterReadingReviewHistoryDto requestDto, String token) {
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
    }

    public MeterReadingReviewActualDto createMeterReadingReviewAllActualDto(String token) {
        int userId = authService.getUserIdFromToken(token);
        MeterReadingReviewActualDto requestDto = new MeterReadingReviewActualDto();
        requestDto.setUserId(userId);
        return requestDto;
    }

    public void updateMeterReadingReviewOnConcreteUtilityDto(MeterReadingReviewActualDto requestDto, String token) {
        meterReadingValidator.isValidMeterReadingUtilityType(requestDto);
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
    }

    public void verifyUserRegisterDto(UserRegisterDto userIncomingDto) {
        userIncomingDtoValidator.isValid(userIncomingDto);
    }
}
