package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserIncomingDtoMapper {
    UserIncomingDtoMapper USER_INCOMING_DTO_MAPPER = Mappers.getMapper(UserIncomingDtoMapper.class);

    User incomingUserDtoToUser(UserIncomingDto userIncomingDto);

    UserIncomingDto userToDto(User user);

}
