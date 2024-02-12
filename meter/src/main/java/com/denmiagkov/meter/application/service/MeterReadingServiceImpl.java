package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewActualDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewForMonthDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingReviewHistoryDto;
import com.denmiagkov.meter.application.dto.incoming.MeterReadingSubmitDto;
import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.aspect.annotations.Audit;
import com.denmiagkov.meter.domain.*;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

import static com.denmiagkov.meter.application.mapper.MeterReadingMapper.METER_READING_OUTGOING_DTO_MAPPER;

/**
 * Сервис подачи показаний
 */
@Audit
public class MeterReadingServiceImpl implements MeterReadingService {

    public static final MeterReadingServiceImpl INSTANCE = new MeterReadingServiceImpl();
    /**
     * Репозиторий данных о показаниях счетчика
     */
    private final MeterReadingRepository meterReadingRepository;
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final UserActivityService activityService;

    public MeterReadingServiceImpl() {
        this.meterReadingRepository = MeterReadingRepositoryImpl.INSTANCE;
        this.activityService = UserActivityServiceImpl.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto submitNewMeterReading(MeterReadingSubmitDto meterReadingDto) {
        MeterReading meterReading = meterReadingRepository.addNewMeterReading(meterReadingDto);
        return METER_READING_OUTGOING_DTO_MAPPER.meterReadingToMeterReadingDto(meterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReadingDto>> getAllReadingsList(int pageSize) {
        List<MeterReading> meterReadingList = meterReadingRepository.getAllMeterReadings();
        List<MeterReadingDto> meterReadingDtoList =
                METER_READING_OUTGOING_DTO_MAPPER.listMeterReadingToListMeterReadingDto(meterReadingList);
        return ListUtils.partition(meterReadingDtoList, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto getActualMeterReadingOnExactUtilityByUser(MeterReadingReviewActualDto requestDto) {
        MeterReading newMeterReading = meterReadingRepository.getActualMeterReadingOnExactUtility(
                requestDto.getUserId(), requestDto.getUtilityId());
        return METER_READING_OUTGOING_DTO_MAPPER.meterReadingToMeterReadingDto(newMeterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(MeterReadingReviewActualDto requestDto) {
        List<MeterReading> meterReadings = meterReadingRepository.getActualMeterReadingsOnAllUtilitiesByUser(requestDto.getUserId());
        return METER_READING_OUTGOING_DTO_MAPPER.listMeterReadingToListMeterReadingDto(meterReadings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReadingDto>> getMeterReadingsHistoryByUser(MeterReadingReviewHistoryDto requestDto, int pageSize) {
        List<MeterReading> meterReadingHistory = meterReadingRepository.getMeterReadingsHistory(requestDto.getUserId());
        List<MeterReadingDto> meterReadingHistoryDto =
                METER_READING_OUTGOING_DTO_MAPPER.listMeterReadingToListMeterReadingDto(meterReadingHistory);
        return ListUtils.partition(meterReadingHistoryDto, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getReadingsForMonthByUser(MeterReadingReviewForMonthDto requestDto) {
        List<MeterReading> readingsForMonth = meterReadingRepository.getMeterReadingsForExactMonthByUser(
                requestDto.getUserId(),
                requestDto.getYear(),
                requestDto.getMonth()
        );
        return METER_READING_OUTGOING_DTO_MAPPER.listMeterReadingToListMeterReadingDto(readingsForMonth);
    }
}
