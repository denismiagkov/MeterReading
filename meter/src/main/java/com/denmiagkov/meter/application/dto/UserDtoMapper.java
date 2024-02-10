package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserDtoMapper {
    UserDtoMapper USER_DTO_MAPPER = Mappers.getMapper(UserDtoMapper.class);
    UserDto userToUserDto(User user);

    Set<UserDto> usersToUserDtos(Set<User> users);
}
