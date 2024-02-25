package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.dto.incoming.*;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.mapper.MeterReadingMapper;
import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.aspects.annotations.Audit;
import com.denmiagkov.meter.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис подачи показаний
 */
@Audit
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
    public MeterReadingDto submitNewMeterReading(SubmitNewMeterReadingDto meterReadingDto) {
        MeterReading meterReading = meterReadingRepository.addNewMeterReading(meterReadingDto);
        return mapper.meterReadingToMeterReadingDto(meterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getAllMeterReadingsList(int page, int pageSize) {
        List<MeterReading> meterReadingList = meterReadingRepository.findAllMeterReadings(page, pageSize);
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
    public List<MeterReadingDto> getMeterReadingsHistoryByUser(ReviewMeterReadingHistoryDto requestDto) {
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
    public List<MeterReadingDto> getReadingsForMonthByUser(ReviewMeterReadingForMonthDto requestDto) {
        List<MeterReading> readingsForMonth = meterReadingRepository.findMeterReadingsForExactMonthByUser(
                requestDto.getUserId(),
                requestDto.getYear(),
                requestDto.getMonth()
        );
        return mapper.listMeterReadingToListMeterReadingDto(readingsForMonth);
    }
}
