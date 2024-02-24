package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.mapper.MeterReadingMapper;
import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.domain.*;
import com.denmiagkov.starter.audit.aspect.annotations.Audit;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис подачи показаний
 */
@Audit
@Service
@AllArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final MeterReadingMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto submitNewMeterReading(SubmitNewMeterReadingDto meterReadingDto) {
        MeterReading meterReading = mapper.meterReadingDtoToMeterReading(meterReadingDto);
        meterReading = meterReadingRepository.addNewMeterReading(meterReading);
        return mapper.meterReadingToMeterReadingDto(meterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getAllMeterReadingsList(Pageable pageable) {
        List<MeterReading> meterReadingList = meterReadingRepository.findAllMeterReadings(pageable);
        return mapper.listMeterReadingToListMeterReadingDto(meterReadingList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto getActualMeterReadingOnExactUtilityByUser(ReviewActualMeterReadingDto requestDto) {
        MeterReading newMeterReading = meterReadingRepository.findActualMeterReadingOnExactUtility(
                requestDto.getUserId(), requestDto.getUtilityId());
        return mapper.meterReadingToMeterReadingDto(newMeterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(ReviewActualMeterReadingDto requestDto) {
        List<MeterReading> meterReadings = meterReadingRepository.findActualMeterReadingsOnAllUtilitiesByUser(requestDto.getUserId());
        return mapper.listMeterReadingToListMeterReadingDto(meterReadings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getMeterReadingsHistoryByUser(ReviewMeterReadingHistoryDto requestDto, Pageable pageable) {
        List<MeterReading> meterReadingHistory = meterReadingRepository.findMeterReadingsHistory(
                requestDto.getUserId(), pageable);
        return mapper.listMeterReadingToListMeterReadingDto(meterReadingHistory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getReadingsForMonthByUser(ReviewMeterReadingForMonthDto requestDto) {
        List<MeterReading> readingsForMonth = meterReadingRepository.findMeterReadingsForExactMonthByUser(
                requestDto.getUserId(),
                requestDto.getYear(),
                requestDto.getMonth()
        );
        return mapper.listMeterReadingToListMeterReadingDto(readingsForMonth);
    }
}
