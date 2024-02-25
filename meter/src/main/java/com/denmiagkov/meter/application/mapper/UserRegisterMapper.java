package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.incoming.RegisterUserDto;
import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер пользователя и входящего ДТО для регистрации
 */
@Mapper
public interface UserRegisterMapper {
    UserRegisterMapper INSTANCE = Mappers.getMapper(UserRegisterMapper.class);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "password", target = "password")
    User incomingUserDtoToUser(RegisterUserDto registerUserDto);
}
