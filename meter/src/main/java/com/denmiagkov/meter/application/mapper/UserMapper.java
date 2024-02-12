package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * Маппер объекта пользователя и его исходящего ДТО
 */
@Mapper
public interface UserMapper {
    UserMapper USER_OUTGOING_DTO_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "login", target = "login")
    UserDto userToUserDto(User user);

    Set<UserDto> usersToUserDtos(Set<User> users);
}
