package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserAction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserActionDtoMapper {
    UserActionDtoMapper INSTANCE = Mappers.getMapper(UserActionDtoMapper.class);

    UserActionDto userActionToUserActionDto(UserAction action);

    List<UserActionDto> userActionsToUserActionDtos(List<UserAction> actions);
}