package com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl;

import com.denmiagkov.meter.application.dto.incoming.RegisterUserDto;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.DtoValidator;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputLoginException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputNameException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputPasswordException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.IncorrectInputPhoneNumberException;
import org.springframework.stereotype.Component;

/**
 * Класс, валидирующий сведения о новом пользователе при его регистрации в приложении
 */
@Component
public class UserIncomingDtoValidatorImpl implements DtoValidator<RegisterUserDto> {

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
    public boolean isValid(RegisterUserDto userDto) {
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
