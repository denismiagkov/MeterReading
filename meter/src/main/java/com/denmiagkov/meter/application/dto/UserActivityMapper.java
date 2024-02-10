package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.UserAction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserActivityMapper {

    UserActivityMapper INSTANCE = Mappers.getMapper(UserActivityMapper.class);

    UserActivityDto userActivityToUserActivityDto(UserAction userAction);

    List<UserAction> listUserActivityToListUserActivityDto(List<UserAction> meterReadings);

}
