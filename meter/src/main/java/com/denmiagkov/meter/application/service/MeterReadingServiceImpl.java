package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.MeterReadingDtoMapper;
import com.denmiagkov.meter.application.dto.MeterReadingSubmitDto;
import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.domain.*;
import org.apache.commons.collections4.ListUtils;

import java.util.List;
import java.util.Map;

/**
 * Сервис подачи показаний
 */

public class MeterReadingServiceImpl implements MeterReadingService {
    /**
     * Репозиторий данных о показаниях счетчика
     */
    private final MeterReadingRepository meterReadingRepository;
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final UserActivityService activityService;

    public MeterReadingServiceImpl(MeterReadingRepository meterReadingRepository, UserActivityService activityService) {
        this.meterReadingRepository = meterReadingRepository;
        this.activityService = activityService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void submitNewMeterReading(MeterReadingSubmitDto meterReading) {
        activityService.registerUserAction(meterReading.getUserId(), ActionType.SUBMIT_NEW_READING);
        meterReadingRepository.addNewMeterReading(meterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReadingSubmitDto>> getAllReadingsList(int pageSize) {
        List<MeterReading> meterReadingList = meterReadingRepository.getAllMeterReadings();
        List<MeterReadingSubmitDto> meterReadingSubmitDtoList =
                MeterReadingDtoMapper.INSTANCE.listMeterReadingToListMeterReadingDto(meterReadingList);
        List<List<MeterReadingSubmitDto>> meterReadingPages = ListUtils.partition(meterReadingSubmitDtoList, pageSize);
        return meterReadingPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingSubmitDto getActualMeterReadingOnExactUtilityByUser(MeterReadingSubmitDto meterReading) {
        activityService.registerUserAction(meterReading.getUserId(), ActionType.REVIEW_ACTUAL_READING);
        MeterReading newMeterReading = meterReadingRepository.getActualMeterReadingOnExactUtility(
                meterReading.getUserId(), meterReading.getUtilityId());
        return MeterReadingDtoMapper.INSTANCE.meterReadingToMeterReadingDto(newMeterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingSubmitDto> getActualMeterReadingsOnAllUtilitiesByUser(int userId) {
        activityService.registerUserAction(userId, ActionType.REVIEW_ACTUAL_READING);
        List<MeterReading> meterReadings = meterReadingRepository.getActualMeterReadingsOnAllUtilitiesByUser(userId);
        return MeterReadingDtoMapper.INSTANCE.listMeterReadingToListMeterReadingDto(meterReadings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReadingSubmitDto>> getMeterReadingsHistoryByUser(int userId, int pageSize) {
        activityService.registerUserAction(userId, ActionType.REVIEW_READINGS_HISTORY);
        List<MeterReading> meterReadingHistory = meterReadingRepository.getMeterReadingsHistory(userId);
        List<MeterReadingSubmitDto> meterReadingSubmitDtoHistory =
                MeterReadingDtoMapper.INSTANCE.listMeterReadingToListMeterReadingDto(meterReadingHistory);
        List<List<MeterReadingSubmitDto>> meterReadingHistoryByPages =
                ListUtils.partition(meterReadingSubmitDtoHistory, pageSize);
        return meterReadingHistoryByPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingSubmitDto> getReadingsForMonthByUser(int userId, Map<String, Integer> month) {
        activityService.registerUserAction(userId, ActionType.REVIEW_READINGS_FOR_MONTH);
        List<MeterReading> readingsForMonth = meterReadingRepository.getMeterReadingsForExactMonthByUser(userId, month);
        return MeterReadingDtoMapper.INSTANCE.listMeterReadingToListMeterReadingDto(readingsForMonth);
    }
}
