package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDtoMapper;
import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.application.dto.incoming.UserRegisterDto;
import com.denmiagkov.meter.application.dto.incoming.UserLoginDtoMapper;
import com.denmiagkov.meter.application.service.exception.AdminNotAuthorizedException;
import com.denmiagkov.meter.application.repository.UserRepositoryImpl;
import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
import com.denmiagkov.meter.application.service.exception.LoginAlreadyInUseException;
import com.denmiagkov.meter.application.service.exception.UserAlreadyExistsException;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.domain.*;

import java.util.Set;

import static com.denmiagkov.meter.application.dto.incoming.UserRegisterDtoMapper.USER_INCOMING_DTO_MAPPER;

/**
 * Класс реализует логику обработки данных о пользователях
 */
public class UserServiceImpl implements UserService {

    public static final UserServiceImpl INSTANCE = new UserServiceImpl();
    /**
     * Репозиторий данных о пользователе
     */
    private final UserRepository userRepository;
    /**
     * Сервис обработки пользовательских действий в приложении
     */
    private final UserActivityService activityService;

    public UserServiceImpl() {
        this.userRepository = UserRepositoryImpl.INSTANCE;
        this.activityService = UserActivityServiceImpl.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto registerUser(UserRegisterDto userIncomingDto) {
        User user = USER_INCOMING_DTO_MAPPER.incomingUserDtoToUser(userIncomingDto);
        setUserRole(userIncomingDto, user);
        UserDto userOutgoingDto = addNewUserToDatabase(user);
        activityService.registerUserAction(userIncomingDto);
        return userOutgoingDto;
    }

    private UserDto addNewUserToDatabase(User user) {
        if (!userRepository.isExistUser(user)) {
            if (!userRepository.isExistLogin(user.getLogin())) {
                int userId = userRepository.addUser(user);
                user.setId(userId);
                return UserDtoMapper.USER_OUTGOING_DTO_MAPPER.userToUserDto(user);
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
        return UserLoginDtoMapper.USER_LOGIN_DTO_MAPPER.userToUserLoginDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<UserDto> getAllUsers() {
        Set<User> users = userRepository.getAllUsers();
        return UserDtoMapper.USER_OUTGOING_DTO_MAPPER.usersToUserDtos(users);
    }
}
