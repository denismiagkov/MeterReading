package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.RegisterUserDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.mapper.UserLoginMapper;
import com.denmiagkov.meter.application.mapper.UserMapper;
import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.mapper.UserRegisterMapper;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.application.service.exceptions.AuthenticationFailedException;
import com.denmiagkov.meter.application.service.exceptions.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.service.exceptions.UserAlreadyExistsException;
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
    private final UserRegisterMapper incomingDtoMapper;
    private final UserMapper outgoingDtoMapper;
    private final UserLoginMapper loginMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserActivityService activityService) {
        this.userRepository = userRepository;
        this.activityService = activityService;
        this.incomingDtoMapper = UserRegisterMapper.INSTANCE;
        this.outgoingDtoMapper = UserMapper.INSTANCE;
        this.loginMapper = UserLoginMapper.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto registerUser(RegisterUserDto registerDto) {
        User user = incomingDtoMapper.incomingUserDtoToUser(registerDto);
        UserDto userOutgoingDto = addNewUserToDatabase(user);
        registerDto.setUserId(user.getId());
        activityService.registerUserAction(registerDto);
        return userOutgoingDto;
    }

    private UserDto addNewUserToDatabase(User user) {
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(user.getLogin())) {
                int userId = userRepository.saveUser(user);
                user.setId(userId);
                return outgoingDtoMapper.userToUserDto(user);
            } else {
                throw new LoginAlreadyInUseException(user.getLogin());
            }
        } else {
            throw new UserAlreadyExistsException(user);
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
    public Set<UserDto> getAllUsers(Pageable pageable) {
        Set<User> users = userRepository.findAllUsers(pageable);
        return outgoingDtoMapper.usersToUserDtos(users);
    }
}