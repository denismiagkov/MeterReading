package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.dto.UserIncomingDto;
import com.denmiagkov.meter.application.dto.UserLoginDto;
import com.denmiagkov.meter.application.exception.AdminNotAuthorizedException;
import com.denmiagkov.meter.infrastructure.in.validator.exception.AuthenticationFailedException;
import com.denmiagkov.meter.application.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.domain.*;
import lombok.AllArgsConstructor;

import java.util.Set;

import static com.denmiagkov.meter.application.dto.UserIncomingDtoMapper.USER_INCOMING_DTO_MAPPER;
import static com.denmiagkov.meter.application.dto.UserDtoMapper.USER_DTO_MAPPER;
import static com.denmiagkov.meter.application.dto.UserLoginDtoMapper.USER_LOGIN_DTO_MAPPER;

/**
 * Класс реализует логику обработки данных о пользователях
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * Репозиторий данных о пользователе
     */
    private final UserRepository userRepository;
    /**
     * Сервис данных о действиях пользователя
     */
    private final UserActivityService activityService;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto registerUser(UserIncomingDto userInDto) {
        User user = USER_INCOMING_DTO_MAPPER.incomingUserDtoToUser(userInDto);
        setUserRole(userInDto, user);
        return addNewUserToDatabase(user);
    }

    private UserDto addNewUserToDatabase(User user) {
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(user.getLogin())) {
                int userId = userRepository.addUser(user);
                user.setId(userId);
                UserDto userDto = USER_DTO_MAPPER.userToUserDto(user);
                activityService.registerUserAction(userDto.getId(), ActionType.REGISTRATION);
                return userDto;
            } else {
                throw new LoginAlreadyInUseException(user.getLogin());
            }
        } else {
            throw new UserAlreadyExistsException(user);
        }
    }

    private void setUserRole(UserIncomingDto userDto, User user) {
        if (user.getRole() != null &&
            user.getRole().equals(UserRole.ADMIN) &&
            (userDto.getAdminPassword() == null ||
             !userDto.getAdminPassword().equals(user.getADMIN_PASSWORD()))) {
            throw new AdminNotAuthorizedException();
        } else if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLoginDto getPasswordByLogin(String login) {
        User user = userRepository.findUserByLogin(login)
                .orElseThrow(AuthenticationFailedException::new);
        UserLoginDto loginDto = USER_LOGIN_DTO_MAPPER.userToUserLoginDto(user);
        UserDto userDto = USER_DTO_MAPPER.userToUserDto(user);
        if (!user.getRole().equals(UserRole.ADMIN)) {
            activityService.registerUserAction(userDto.getId(), ActionType.AUTHENTICATION);
        }
        return loginDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<UserDto> getAllUsers() {
        Set<User> users = userRepository.getAllUsers();
        return USER_DTO_MAPPER.usersToUserDtos(users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordExit(UserDto user) {
//        UserActivity userActivity = new UserActivity(user, ActivityType.EXIT);
//        activityService.addActivity(userActivity);
    }


}
