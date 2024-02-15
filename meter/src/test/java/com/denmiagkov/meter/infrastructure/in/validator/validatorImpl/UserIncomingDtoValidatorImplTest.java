package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.domain.UserRole;
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
        UserRegisterDto registerDto = new UserRegisterDto("Alex2", "+7123456789", "Moscow", UserRole.USER,
                "user", "123456789", null);

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputNameException.class);
    }

    @Test
    @DisplayName("Throws exception when name contains not only of letters")
    void isValid_IncorrectPhone() {
        UserRegisterDto registerDto = new UserRegisterDto("Alex", "-7123456789", "Moscow", UserRole.USER,
                "user", "123456789", null);

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputPhoneNumberException.class);
    }

    @Test
    @DisplayName("Throws exception when login is blank")
    void isValid_IncorrectPhone_InvalidLogin() {
        UserRegisterDto registerDto = new UserRegisterDto("Alex", "+7123456789", "Moscow", UserRole.USER,
                "", "123", null);

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputLoginException.class);
    }

    @Test
    @DisplayName("Throws exception when password is too short")
    void isValid_IncorrectPhone_InvalidPassword() {
        UserRegisterDto registerDto = new UserRegisterDto("Alex", "+7123456789", "Moscow", UserRole.USER,
                "user", "123", null);

        assertThatThrownBy(() -> validator.isValid(registerDto))
                .isInstanceOf(IncorrectInputPasswordException.class);
    }
}