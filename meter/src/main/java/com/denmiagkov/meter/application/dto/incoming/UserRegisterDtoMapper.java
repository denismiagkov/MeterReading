package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Маппер пользователя и входящего ДТО для регистрации
 */
@Mapper
public interface UserRegisterDtoMapper {
    UserRegisterDtoMapper USER_INCOMING_DTO_MAPPER = Mappers.getMapper(UserRegisterDtoMapper.class);

    User incomingUserDtoToUser(UserRegisterDto userRegisterDto);

    UserRegisterDto userToDto(User user);

}
