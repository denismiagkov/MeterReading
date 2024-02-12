package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * Маппер объекта пользователя и его исходящего ДТО
 */
@Mapper
public interface UserDtoMapper {
    UserDtoMapper USER_OUTGOING_DTO_MAPPER = Mappers.getMapper(UserDtoMapper.class);

    UserDto userToUserDto(User user);

    Set<UserDto> usersToUserDtos(Set<User> users);
}
