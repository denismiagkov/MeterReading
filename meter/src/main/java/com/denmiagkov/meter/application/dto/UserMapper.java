package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    Set<UserDto> userSetToUserDtoSet(Set<User> users);
}