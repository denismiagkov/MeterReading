package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserLoginDtoMapper {
    UserLoginDtoMapper USER_LOGIN_DTO_MAPPER = Mappers.getMapper(UserLoginDtoMapper.class);

    UserLoginDto userToUserLoginDto(User user);

    User userLoginDtoToUser(UserLoginDto userLoginDto);

}
