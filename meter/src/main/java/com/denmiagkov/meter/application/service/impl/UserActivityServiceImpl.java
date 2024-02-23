package com.denmiagkov.meter.application.service.impl;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.mapper.UserActionMapper;
import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.starter.audit.dto.IncomingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис, реализующий логику обработки данных о действиях пользователей в системе
 */
@Service
public class UserActivityServiceImpl implements UserActivityService {
    /**
     * Репозиторий данных о действиях пользователя
     */
    private final ActivityRepository activityRepository;
    private final UserActionMapper mapper;

    @Autowired
    public UserActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
        this.mapper = UserActionMapper.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerUserAction(IncomingDto incomingDto) {
        UserAction userAction = new UserAction(incomingDto.getUserId(),
                (ActionType) incomingDto.getAction());
        activityRepository.addUserAction(userAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserActionDto> getUserActivitiesList(Pageable pageable) {
        List<UserAction> userActions = activityRepository.findAllUsersActions(pageable);
        return mapper.userActionsToUserActionDtos(userActions);
    }
}
