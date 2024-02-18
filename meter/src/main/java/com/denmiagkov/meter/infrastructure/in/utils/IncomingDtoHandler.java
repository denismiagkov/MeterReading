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

    public void updateSubmitNewMeterReadingDto(SubmitNewMeterReadingDto meterReading, String token) {
        int userId = authService.getUserIdFromToken(token);
        meterReading.setUserId(userId);
        meterReading.setDate(LocalDateTime.now());
        meterReadingValidator.isValid(meterReading);
    }

    public ReviewMeterReadingForMonthDto createReviewMeterReadingsForMonthDto(int month, int year, String token) {
        meterReadingValidator.isValidMonth(month, year);
        ReviewMeterReadingForMonthDto requestDto = new ReviewMeterReadingForMonthDto();
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
        requestDto.setMonth(month);
        requestDto.setYear(year);
        return requestDto;
    }

    public ReviewMeterReadingHistoryDto createMeterReadingReviewHistoryDto(int page, int pageSize, String token) {
        ReviewMeterReadingHistoryDto requestDto = new ReviewMeterReadingHistoryDto();
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
        requestDto.setPage(page);
        requestDto.setPageSize(pageSize);
        return requestDto;
    }

    public ReviewActualMeterReadingDto createReviewAllActualMeterReadingsDto(String token) {
        int userId = authService.getUserIdFromToken(token);
        ReviewActualMeterReadingDto requestDto = new ReviewActualMeterReadingDto();
        requestDto.setUserId(userId);
        return requestDto;
    }

    public ReviewActualMeterReadingDto createReviewMeterReadingOnConcreteUtilityDto(int utilityId, String token) {
        ReviewActualMeterReadingDto requestDto = new ReviewActualMeterReadingDto();
        requestDto.setUtilityId(utilityId);
        meterReadingValidator.isValidMeterReadingUtilityType(requestDto);
        int userId = authService.getUserIdFromToken(token);
        requestDto.setUserId(userId);
        return requestDto;
    }

    public void verifyRegisterUserDto(RegisterUserDto userIncomingDto) {
        userIncomingDtoValidator.isValid(userIncomingDto);
    }
}
