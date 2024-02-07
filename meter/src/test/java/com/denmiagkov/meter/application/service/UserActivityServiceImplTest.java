package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.UserActivity;
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
class UserActivityServiceImplTest {
    @Mock
    ActivityRepository activityRepository;
    @InjectMocks
    UserActivityServiceImpl activityService;

    @BeforeEach
    void setup() {
        activityService = new UserActivityServiceImpl(activityRepository);
    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object")
    void addActivity() {
        UserActivity userActivity = mock(UserActivity.class);
        activityService.addActivity(userActivity);

        verify(activityRepository, times(1))
                .addActivity(userActivity);
    }

    @Test
    @DisplayName("Dependent object returns appropriate list")
    void getUserActivitiesList() {
        List<UserActivity> activitiesDummy = mock(ArrayList.class);
        when(activityRepository.getActivitiesList()).thenReturn(activitiesDummy);

        List<UserActivity> activities = activityService.getUserActivitiesList();

        assertThat(activities).isEqualTo(activitiesDummy);
    }
}