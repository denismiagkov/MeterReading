package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.incoming.ReviewMeterReadingHistoryDto;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.mapper.UserActionMapper;
import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.application.service.impl.UserActivityServiceImpl;
import com.denmiagkov.meter.domain.UserAction;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserActionServiceImplTest {
    @Mock
    ActivityRepository activityRepository;
    @Mock
    UserActionMapper userActionMapper;
    @InjectMocks
    UserActivityServiceImpl activityService;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object")
    void addActivity() {
        ReviewMeterReadingHistoryDto dto = new ReviewMeterReadingHistoryDto();
        dto.setUserId(2);

        activityService.registerUserAction(dto);

        verify(activityRepository, times(1))
                .addUserAction(any(UserAction.class));
    }

    @Test
    @DisplayName("Dependent object returns appropriate list")
    void getUserActivitiesList() {
        List<UserAction> activitiesDummy = mock(ArrayList.class);
        Pageable pageable = mock(Pageable.class);
        when(activityRepository.findAllUsersActions(pageable)).thenReturn(activitiesDummy);
        List<UserActionDto> userActionDtos = mock(ArrayList.class);
        when(userActionMapper.userActionsToUserActionDtos(activitiesDummy))
                .thenReturn(userActionDtos);
        List<List<UserActionDto>> userActionsPaginated = mock((ArrayList.class));
        when(ListUtils.partition(userActionDtos, 2))
                .thenReturn(userActionsPaginated);

        List<UserActionDto> activities = activityService.getUserActivitiesList(Pageable.of(0, 10));

        assertThat(activities).isEqualTo(userActionsPaginated);
    }
}