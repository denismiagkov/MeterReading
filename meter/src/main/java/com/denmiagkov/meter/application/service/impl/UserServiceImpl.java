package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.dto.incoming.PaginationDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.mapper.UserLoginMapper;
import com.denmiagkov.meter.application.mapper.UserMapper;
import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.application.mapper.UserRegisterMapper;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.exception.AdminNotAuthorizedException;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
import com.denmiagkov.meter.application.service.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.service.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Класс реализует логику обработки данных о пользователях
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * Репозиторий данных о пользователе
     */
    private final UserRepository userRepository;
    /**
     * Сервис обработки пользовательских действий в приложении
     */
    private final UserActivityService activityService;
    UserRegisterMapper incomingDtoMapper = UserRegisterMapper.INSTANCE;
    UserMapper outgoingDtoMapper = UserMapper.INSTANCE;
    UserLoginMapper loginMapper = UserLoginMapper.INSTANCE;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserActivityService activityService) {
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto registerUser(UserRegisterDto userIncomingDto) {
        User user = incomingDtoMapper.incomingUserDtoToUser(userIncomingDto);
        setUserRole(userIncomingDto, user);
        UserDto userOutgoingDto = addNewUserToDatabase(user);
        userIncomingDto.setUserId(user.getId());
        activityService.registerUserAction(userIncomingDto);
        return userOutgoingDto;
    }

    private UserDto addNewUserToDatabase(User user) {
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(user.getLogin())) {
                int userId = userRepository.addUser(user);
                user.setId(userId);
                return outgoingDtoMapper.userToUserDto(user);
            } else {
                throw new LoginAlreadyInUseException(user.getLogin());
            }
        } else {
            throw new UserAlreadyExistsException(user);
        }
    }

    private void setUserRole(UserRegisterDto userDto, User user) {
        if (user.getRole() != null &&
            user.getRole().equals(UserRole.ADMIN) &&
            (userDto.getAdminPassword() == null ||
             !userDto.getAdminPassword().equals(user.getAdminPassword()))) {
            throw new AdminNotAuthorizedException();
        } else if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
    }

    @Override
    public UserLoginDto getPasswordByLogin(String login) {
        User user = userRepository.findUserByLogin(login)
                .orElseThrow(AuthenticationFailedException::new);
        return loginMapper.userToUserLoginDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<UserDto> getAllUsers(PaginationDto pagination) {
        Set<User> users = userRepository.findAllUsers(pagination.getPageSize(), pagination.getPage());
        return outgoingDtoMapper.usersToUserDtos(users);
    }
}
