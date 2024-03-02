package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

/**
 * Маппер объекта пользователя и его исходящего ДТО
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "login", target = "login")
    UserDto userToUserDto(User user);

    HashSet<UserDto> usersToUserDtos(Set<User> users);
}
