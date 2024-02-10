package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.UserAction;
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
    @InjectMocks
    UserActivityServiceImpl activityService;

    @BeforeEach
    void setup() {
        activityService = new UserActivityServiceImpl(activityRepository);
    }

//    @Test
//    @DisplayName("Method invokes appropriate method on dependent object")
//    void addActivity() {
//        UserAction userAction = mock(UserAction.class);
//        activityService.addActivity(userAction);
//
//        verify(activityRepository, times(1))
//                .addActivity(userAction);
//    }

//    @Test
//    @DisplayName("Dependent object returns appropriate list")
//    void getUserActivitiesList() {
//        List<UserAction> activitiesDummy = mock(ArrayList.class);
//        when(activityRepository.getActivitiesList()).thenReturn(activitiesDummy);
//
//        List<UserAction> activities = activityService.getUserActivitiesList();
//
//        assertThat(activities).isEqualTo(activitiesDummy);
//    }
}