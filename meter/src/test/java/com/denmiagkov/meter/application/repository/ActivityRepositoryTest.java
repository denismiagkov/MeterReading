package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserActivityServiceImpl;
import com.denmiagkov.meter.application.service.UserServiceImpl;
import com.denmiagkov.meter.domain.Activity;
import com.denmiagkov.meter.domain.ActivityType;
import com.denmiagkov.meter.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ActivityRepositoryTest {
    ActivityRepository activityRepository = new ActivityRepository();
    User user;

    @BeforeEach
    void setUp() {
        user = new User("John", "11-22-33", "Moscow", "user", "123");
        //storage.addUser(user);
    }

    @Test
    void addActivity() {
        Activity activity = mock(Activity.class);
        boolean result = activityRepository.addActivity(activity);

        assertThat(result).isTrue();
    }

    @Test
    @Disabled
    void getActivityList() {
        UserRepository userRepository = new UserRepository();
        UserActivityService activityService = new UserActivityServiceImpl(activityRepository);
        UserServiceImpl userService = new UserServiceImpl(userRepository, activityService);
        userService.registerUser(user.getName(), user.getPhone(), user.getAddress(), user.getLogin(), user.getPassword());
        userService.authorizeUser("user", "123");
        userService.recordExit(user);

        List<Activity> userActivities = activityRepository.getActivitiesList();

        assertAll(
                () -> assertThat(userActivities)
                        .isNotNull()
                        .hasSize(3),
                () -> assertThat(userActivities.get(0).getAction())
                        .isEqualTo(ActivityType.REGISTRATION),
                () -> assertThat(userActivities.get(1).getAction())
                        .isEqualTo(ActivityType.AUTHORIZATION),
                () -> assertThat(userActivities.get(2).getAction())
                        .isEqualTo(ActivityType.EXIT));
    }
}