package com.denmiagkov.meter.application.dto;

import com.denmiagkov.meter.domain.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MeterReadingMapper {

    MeterReadingMapper INSTANCE = Mappers.getMapper(MeterReadingMapper.class);

    MeterReadingDto meterReadingToMeterReadingDto(MeterReading meterReading);

    List<MeterReading> listMeterReadingToListMeterReadingDto(List<MeterReading> meterReadings);
}
