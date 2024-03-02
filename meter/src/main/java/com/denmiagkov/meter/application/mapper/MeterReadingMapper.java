package com.denmiagkov.meter.application.mapper;

import com.denmiagkov.meter.application.dto.incoming.SubmitNewMeterReadingDto;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.domain.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Маппер объекта показания счетчика и его исходящего ДТО
 */
@Mapper(componentModel = "spring")
public interface MeterReadingMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "utilityId", target = "utilityId")
    @Mapping(source = "value", target = "value")
    MeterReadingDto meterReadingToMeterReadingDto(MeterReading meterReading);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "utilityId", target = "utilityId")
    @Mapping(source = "value", target = "value")
    MeterReading meterReadingDtoToMeterReading(SubmitNewMeterReadingDto meterReadingDto);

    List<MeterReadingDto> listMeterReadingToListMeterReadingDto(List<MeterReading> meterReadings);
}
