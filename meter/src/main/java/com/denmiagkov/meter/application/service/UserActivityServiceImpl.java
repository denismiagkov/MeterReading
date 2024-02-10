package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.UserActionDto;
import com.denmiagkov.meter.application.dto.UserActionDtoMapper;
import com.denmiagkov.meter.application.dto.UserDto;
import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * Сервис, реализующий логику обработки данных о действиях пользователей в системе
 */
@AllArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final ActivityRepository activityRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerUserAction(int userId, ActionType actionType) {
        UserAction userAction = new UserAction(userId, actionType);
        activityRepository.addActivity(userAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<UserActionDto>> getUserActivitiesList(int pageSize) {
        List<UserAction> userActions = activityRepository.getActivitiesList();
        List<UserActionDto> userActionDtos = UserActionDtoMapper.INSTANCE.userActionsToUserActionDtos(userActions);
        List<List<UserActionDto>> userActionsPaginated = ListUtils.partition(userActionDtos, pageSize);
        return userActionsPaginated;
    }
}
