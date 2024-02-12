package com.denmiagkov.meter.application.dto.outgoing;

import com.denmiagkov.meter.domain.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Маппер объекта показания счетчика и его исходящего ДТО
 */
@Mapper
public interface MeterReadingDtoMapper {

    MeterReadingDtoMapper METER_READING_OUTGOING_DTO_MAPPER = Mappers.getMapper(MeterReadingDtoMapper.class);

    MeterReadingDto meterReadingToMeterReadingDto(MeterReading meterReading);

    List<MeterReadingDto> listMeterReadingToListMeterReadingDto(List<MeterReading> meterReadings);
}
