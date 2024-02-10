package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MeterReadingDtoMapper {

    MeterReadingDtoMapper INSTANCE = Mappers.getMapper(MeterReadingDtoMapper.class);

    MeterReadingSubmitDto meterReadingToMeterReadingDto(MeterReading meterReading);

    List<MeterReadingSubmitDto> listMeterReadingToListMeterReadingDto(List<MeterReading> meterReadings);
}
