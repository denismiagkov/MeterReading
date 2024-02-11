package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserLoginDtoMapper {
    UserLoginDtoMapper USER_LOGIN_DTO_MAPPER = Mappers.getMapper(UserLoginDtoMapper.class);

    default UserDtoLogin userToUserLoginDto(User user){
        if (user == null) {
            return null;
        } else {
            UserDtoLogin userLoginDto = new UserDtoLogin();
            userLoginDto.setUserId(user.getId());
            userLoginDto.setRole(user.getRole());
            userLoginDto.setLogin(user.getLogin());
            userLoginDto.setPassword(user.getPassword());
            return userLoginDto;
        }
    }

    User userLoginDtoToUser(UserDtoLogin userLoginDto);

}
