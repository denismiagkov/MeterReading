package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.domain.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Маппер объекта показания счетчика и его исходящего ДТО
 */
@Mapper
public interface MeterReadingMapper {

    MeterReadingMapper METER_READING_OUTGOING_DTO_MAPPER = Mappers.getMapper(MeterReadingMapper.class);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "utilityId", target = "utilityId")
    @Mapping(source = "value", target = "value")
    MeterReadingDto meterReadingToMeterReadingDto(MeterReading meterReading);

    List<MeterReadingDto> listMeterReadingToListMeterReadingDto(List<MeterReading> meterReadings);
}
