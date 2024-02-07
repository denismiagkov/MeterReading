package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.UserActivity;
import com.denmiagkov.meter.domain.ActivityType;
import com.denmiagkov.meter.utils.ConnectionManager;
import com.denmiagkov.meter.utils.LiquibaseManager;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserActivityRepositoryImplTest {
    ActivityRepositoryImpl activityRepository;
    Connection connection;

    @BeforeAll
    static void beforeAll() {
        PostgresContainerManager.startContainer();
    }

    @AfterAll
    static void afterAll() {
        PostgresContainerManager.stopContainer();
    }

    @BeforeEach
    void setUp() throws SQLException {
        LiquibaseManager.startLiquibase();
        activityRepository = new ActivityRepositoryImpl();
        connection = ConnectionManager.open();
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    @DisplayName("Returns true if given activity is added to database")
    void addActivity() {
        UserActivity userActivity = UserActivity.builder()
                .userId(1)
                .dateTime(LocalDateTime.of(2024, 2, 4, 15, 48))
                .action(ActivityType.REVIEW_READINGS_FOR_MONTH)
                .build();

        boolean result = activityRepository.addActivity(userActivity);
        List<UserActivity> activities = activityRepository.getActivitiesList();
        UserActivity testUserActivity = activities.get(activities.size() - 1);

        assertAll(
                () -> assertThat(result).isTrue(),
                () -> assertThat(testUserActivity.getUserId()).isEqualTo(userActivity.getUserId()),
                () -> assertThat(testUserActivity.getDateTime()).isEqualTo(userActivity.getDateTime()),
                () -> assertThat(testUserActivity.getAction()).isEqualTo(userActivity.getAction())
        );
    }

    @Test
    @DisplayName("Returns activities list and verifies its elements")
    void getActivityList() {
        List<UserActivity> userActivities = activityRepository.getActivitiesList();

        assertAll(
                () -> assertThat(userActivities)
                        .isNotNull(),
                () -> assertThat(userActivities.get(0).getAction())
                        .isEqualTo(ActivityType.REGISTRATION),
                () -> assertThat(userActivities.get(1).getAction())
                        .isEqualTo(ActivityType.AUTHENTICATION),
                () -> assertThat(userActivities.get(2).getUserId())
                        .isEqualTo(1));
    }
}