package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.incoming.LoginUserDto;
import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер пользователя и входящего ДТО для аутентификации
 */
@Mapper
public interface UserLoginMapper {
    UserLoginMapper INSTANCE = Mappers.getMapper(UserLoginMapper.class);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "password", target = "password")
    LoginUserDto userToUserLoginDto(User user);
}
