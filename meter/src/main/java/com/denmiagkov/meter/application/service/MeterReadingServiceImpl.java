package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.MeterReadingDto;
import com.denmiagkov.meter.application.dto.MeterReadingMapper;
import com.denmiagkov.meter.application.dto.MeterReadingMapperImpl;
import com.denmiagkov.meter.application.repository.*;
import com.denmiagkov.meter.domain.*;
import lombok.AllArgsConstructor;
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
    public void submitNewMeterReading(MeterReadingDto meterReading) {
        activityService.registerUserAction(meterReading.getUserId(), ActionType.SUBMIT_NEW_READING);
        meterReadingRepository.addNewMeterReading(meterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReadingDto>> getAllReadingsList(int pageSize) {
        List<MeterReading> meterReadingList = meterReadingRepository.getAllMeterReadings();
        List<MeterReadingDto> meterReadingDtoList =
                MeterReadingMapper.INSTANCE.listMeterReadingToListMeterReadingDto(meterReadingList);
        List<List<MeterReadingDto>> meterReadingPages = ListUtils.partition(meterReadingDtoList, pageSize);
        return meterReadingPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterReadingDto getActualMeterReadingOnExactUtilityByUser(MeterReadingDto meterReading) {
        activityService.registerUserAction(meterReading.getUserId(), ActionType.REVIEW_ACTUAL_READING);
        MeterReading newMeterReading = meterReadingRepository.getActualMeterReadingOnExactUtility(
                meterReading.getUserId(), meterReading.getUtilityId());
        return MeterReadingMapper.INSTANCE.meterReadingToMeterReadingDto(newMeterReading);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getActualMeterReadingsOnAllUtilitiesByUser(int userId) {
        //  UserAction userAction = new UserAction(user, ActionType.REVIEW_ACTUAL_READING);
        //  activityService.addActivity(userAction);
        List<MeterReading> meterReadings = meterReadingRepository.getActualMeterReadingsOnAllUtilitiesByUser(userId);
        return MeterReadingMapper.INSTANCE.listMeterReadingToListMeterReadingDto(meterReadings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<MeterReadingDto>> getMeterReadingsHistoryByUser(int userId, int pageSize) {
        //   UserAction userAction = new UserAction(user, ActionType.REVIEW_CONVEYING_READINGS_HISTORY);
        //   activityService.addActivity(userAction);
        List<MeterReading> meterReadingHistory = meterReadingRepository.getMeterReadingsHistory(userId);
        List<MeterReadingDto> meterReadingDtoHistory =
                MeterReadingMapper.INSTANCE.listMeterReadingToListMeterReadingDto(meterReadingHistory);
        List<List<MeterReadingDto>> meterReadingHistoryByPages =
                ListUtils.partition(meterReadingDtoHistory, pageSize);
        return meterReadingHistoryByPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MeterReadingDto> getReadingsForMonthByUser(int userId, Map<String, Integer> month) {
        //   UserAction userAction = new UserAction(user, ActionType.REVIEW_READINGS_FOR_MONTH);
        //  activityService.addActivity(userAction);
        List<MeterReading> readingsForMonth = meterReadingRepository.getMeterReadingsForExactMonthByUser(userId, month);
        return MeterReadingMapper.INSTANCE.listMeterReadingToListMeterReadingDto(readingsForMonth);
    }
}
