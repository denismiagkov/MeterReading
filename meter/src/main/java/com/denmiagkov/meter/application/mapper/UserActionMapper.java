package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.domain.UserAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Маппер объекта действия пользователя в приложении и его исходящего ДТО
 */
@Mapper(componentModel = "spring")
public interface UserActionMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "dateTime", target = "dateTime")
    @Mapping(source = "action", target = "action")
    UserActionDto userActionToUserActionDto(UserAction action);

    List<UserActionDto> userActionsToUserActionDtos(List<UserAction> actions);
}
