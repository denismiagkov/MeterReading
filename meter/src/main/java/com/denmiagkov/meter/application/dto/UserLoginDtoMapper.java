package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserLoginDtoMapper {
    UserLoginDtoMapper INSTANCE = Mappers.getMapper(UserLoginDtoMapper.class);

    default UserLoginDto userToUserLoginDto(User user){
        if (user == null) {
            return null;
        } else {
            UserLoginDto userLoginDto = new UserLoginDto();
            userLoginDto.setUserId(user.getId());
            userLoginDto.setRole(user.getRole());
            userLoginDto.setLogin(user.getLogin());
            userLoginDto.setPassword(user.getPassword());
            return userLoginDto;
        }
    }

    User userLoginDtoToUser(UserLoginDto userLoginDto);

}
