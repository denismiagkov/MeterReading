package com.denmiagkov.meter.infrastructure.in.validator.validatorImpl;

import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.infrastructure.in.validator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.validator.exception.IncorrectInputLoginException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.IncorrectInputNameException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.IncorrectInputPasswordException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.IncorrectInputPhoneNumberException;

/**
 * Класс, валидирующий сведения о новом пользователе при его регистрации в приложении
 */
public class UserIncomingDtoValidatorImpl implements DtoValidator<UserRegisterDto> {
    public static final UserIncomingDtoValidatorImpl INSTANCE = new UserIncomingDtoValidatorImpl();

    private UserIncomingDtoValidatorImpl() {
    }

    private boolean isValidName(String name) {
        return name != null &&
               (name.length() > 1) &&
               name.matches("[a-zA-Zа-яА-Я]+");
    }

    private boolean isValidPhone(String phone) {
        return phone != null &&
               phone.matches("\\+\\d+");
    }

    private boolean isValidLogin(String login) {
        return login != null &&
               !login.isEmpty() &&
               !login.isBlank();
    }

    private boolean isValidPassword(String password) {
        return password != null &&
               (password.length() > 7);
    }

    @Override
    public boolean isValid(UserRegisterDto userDto) {
        if (!isValidName(userDto.getName())) {
            throw new IncorrectInputNameException();
        } else if (!isValidPhone(userDto.getPhone())) {
            throw new IncorrectInputPhoneNumberException();
        } else if (!isValidLogin(userDto.getLogin())) {
            throw new IncorrectInputLoginException();
        } else if (!isValidPassword(userDto.getPassword())) {
            throw new IncorrectInputPasswordException();
        }
        return true;
    }
}
