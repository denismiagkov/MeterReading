package com.denmiagkov.meter.infrastructure.in.dtoValidator.validatorImpl;

import com.denmiagkov.meter.application.dto.incoming.RegisterUserDto;
import com.denmiagkov.meter.domain.UserRole;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl.UserIncomingDtoValidatorImpl;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputLoginException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputNameException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputPasswordException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputPhoneNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIncomingDtoValidatorImplTest {
    UserIncomingDtoValidatorImpl validator = new UserIncomingDtoValidatorImpl();

    @Test
    @DisplayName("Throws exception when name contains not only of letters")
    void isValid_IncorrectName() {
        RegisterUserDto registerDto = new RegisterUserDto("Alex2", "+7123456789", "Moscow",
                "user", "123456789");

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputNameException.class);
    }

    @Test
    @DisplayName("Throws exception when name contains not only of letters")
    void isValid_IncorrectPhone() {
        RegisterUserDto registerDto = new RegisterUserDto("Alex", "-7123456789", "Moscow",
                "user", "123456789");

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputPhoneNumberException.class);
    }

    @Test
    @DisplayName("Throws exception when login is blank")
    void isValid_IncorrectPhone_InvalidLogin() {
        RegisterUserDto registerDto = new RegisterUserDto("Alex", "+7123456789", "Moscow",
                "", "123");

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputLoginException.class);
    }

    @Test
    @DisplayName("Throws exception when password is too short")
    void isValid_IncorrectPhone_InvalidPassword() {
        RegisterUserDto registerDto = new RegisterUserDto("Alex", "+7123456789", "Moscow",
                "user", "123");

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputPasswordException.class);
    }
}