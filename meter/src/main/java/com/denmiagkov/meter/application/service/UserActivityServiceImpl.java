package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.UserActionDto;
import com.denmiagkov.meter.application.dto.UserActionDtoMapper;
import com.denmiagkov.meter.application.dto.incoming.IncomingDto;
import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.application.repository.ActivityRepositoryImpl;
import com.denmiagkov.meter.domain.UserAction;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * Сервис, реализующий логику обработки данных о действиях пользователей в системе
 */

public class UserActivityServiceImpl implements UserActivityService {
    public static final UserActivityServiceImpl INSTANCE = new UserActivityServiceImpl();
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final ActivityRepository activityRepository;

    private UserActivityServiceImpl() {
        this.activityRepository = ActivityRepositoryImpl.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerUserAction(IncomingDto incomingDto) {
        UserAction userAction = new UserAction(incomingDto.getUserId(), incomingDto.getAction());
        activityRepository.addActivity(userAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<UserActionDto>> getUserActivitiesList(int pageSize) {
        List<UserAction> userActions = activityRepository.getActivitiesList();
        List<UserActionDto> userActionDtos = UserActionDtoMapper.USER_ACTION_DTO_MAPPER.userActionsToUserActionDtos(userActions);
        List<List<UserActionDto>> userActionsPaginated = ListUtils.partition(userActionDtos, pageSize);
        return userActionsPaginated;
    }
}
