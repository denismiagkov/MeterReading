package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.mapper.MeterReadingMapper;
import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.aspect.annotations.Audit;
import com.denmiagkov.meter.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис подачи показаний
 */
@Service
public class MeterReadingServiceImpl implements MeterReadingService {
    /**
     * Репозиторий данных о показаниях счетчика
     */
    private final MeterReadingRepository meterReadingRepository;
    private static final MeterReadingMapper mapper = MeterReadingMapper.INSTANCE;

    @Autowired
    public MeterReadingServiceImpl(MeterReadingRepository meterReadingRepository) {
        this.meterReadingRepository = meterReadingRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto submitNewMeterReading(MeterReadingSubmitDto meterReadingDto) {
        MeterReading meterReading = meterReadingRepository.addNewMeterReading(meterReadingDto);
        return mapper.meterReadingToMeterReadingDto(meterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getAllReadingsList(PaginationDto paginationParam) {
        List<MeterReading> meterReadingList = meterReadingRepository.findAllMeterReadings(
                paginationParam.getPageSize(),
                paginationParam.getPage()
        );
        return mapper.listMeterReadingToListMeterReadingDto(meterReadingList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto getActualMeterReadingOnExactUtilityByUser(MeterReadingReviewActualDto requestDto) {
        MeterReading newMeterReading = meterReadingRepository.findActualMeterReadingOnExactUtility(
                requestDto.getUserId(), requestDto.getUtilityId());
        return mapper.meterReadingToMeterReadingDto(newMeterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(MeterReadingReviewActualDto requestDto) {
        List<MeterReading> meterReadings = meterReadingRepository.findActualMeterReadingsOnAllUtilitiesByUser(requestDto.getUserId());
        return mapper.listMeterReadingToListMeterReadingDto(meterReadings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getMeterReadingsHistoryByUser(MeterReadingReviewHistoryDto requestDto) {
        List<MeterReading> meterReadingHistory = meterReadingRepository.findMeterReadingsHistory(
                requestDto.getUserId(),
                requestDto.getPageSize(),
                requestDto.getPage());
        return mapper.listMeterReadingToListMeterReadingDto(meterReadingHistory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getReadingsForMonthByUser(MeterReadingReviewForMonthDto requestDto) {
        List<MeterReading> readingsForMonth = meterReadingRepository.findMeterReadingsForExactMonthByUser(
                requestDto.getUserId(),
                requestDto.getYear(),
                requestDto.getMonth()
        );
        return mapper.listMeterReadingToListMeterReadingDto(readingsForMonth);
    }
}
