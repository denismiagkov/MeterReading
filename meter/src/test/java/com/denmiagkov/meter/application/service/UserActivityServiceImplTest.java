package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.domain.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserActivityServiceImplTest {

    UserActivityServiceImpl activityService;
    ActivityRepository activityRepository;

    @BeforeEach
    void setup(){
        activityRepository = mock(ActivityRepository.class);
        activityService = new UserActivityServiceImpl(activityRepository);
    }

    @Test
    void addActivity() {
    }

    @Test
    void getUserActivitiesList() {
        List<Activity> activitiesDummy = new ArrayList<>();
        when(activityRepository.getActivitiesList()).thenReturn(activitiesDummy);

        List<Activity> activities = activityService.getUserActivitiesList();

        assertThat(activities).isEqualTo(activitiesDummy);
    }
}