package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.incoming.UserLoginDto;
import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер пользователя и входящего ДТО для аутентификации
 */
@Mapper(componentModel = "spring")
public interface UserLoginMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "password", target = "password")
    UserLoginDto userToUserLoginDto(User user);
}
