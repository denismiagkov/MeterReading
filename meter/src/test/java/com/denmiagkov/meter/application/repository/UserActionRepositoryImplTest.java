package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.meter.utils.ConnectionManager;
import com.denmiagkov.meter.utils.LiquibaseManager;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserActionRepositoryImplTest {
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
        activityRepository = ActivityRepositoryImpl.INSTANCE;
        connection = ConnectionManager.open();
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

//    @Test
//    @DisplayName("Returns true if given activity is added to database")
//    void addActivity() {
//        UserAction userAction = UserAction.builder()
//                .userId(1)
//                .dateTime(LocalDateTime.of(2024, 2, 4, 15, 48))
//                .action(ActionType.REVIEW_READINGS_FOR_MONTH)
//                .build();
//
//        boolean result = activityRepository.addActivity(userAction);
//        List<UserAction> activities = activityRepository.getActivitiesList();
//        UserAction testUserAction = activities.get(activities.size() - 1);
//
//        assertAll(
//                () -> assertThat(result).isTrue(),
//                () -> assertThat(testUserAction.getUserId()).isEqualTo(userAction.getUserId()),
//                () -> assertThat(testUserAction.getDateTime()).isEqualTo(userAction.getDateTime()),
//                () -> assertThat(testUserAction.getAction()).isEqualTo(userAction.getAction())
//        );
//    }

    @Test
    @DisplayName("Returns activities list and verifies its elements")
    void getActivityList() {
        List<UserAction> userActivities = activityRepository.getActivitiesList();

        assertAll(
                () -> assertThat(userActivities)
                        .isNotNull(),
                () -> assertThat(userActivities.get(0).getAction())
                        .isEqualTo(ActionType.REGISTRATION),
                () -> assertThat(userActivities.get(1).getAction())
                        .isEqualTo(ActionType.AUTHENTICATION),
                () -> assertThat(userActivities.get(2).getUserId())
                        .isEqualTo(1));
    }
}