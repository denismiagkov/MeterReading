package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.repository.impl.ActivityRepositoryImpl;
import com.denmiagkov.meter.utils.ConnectionManager;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@DirtiesContext
class UserActionRepositoryImplTest {
    @Autowired
    private ActivityRepositoryImpl activityRepository;
    @Autowired
    private ConnectionManager connectionManager;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = connectionManager.open();
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    @Disabled
    @DisplayName("Returns true if given activity is added to database")
    void addActivity() {
        UserAction userAction = new UserAction(35, 1,
                LocalDateTime.of(2024, 2, 4, 15, 48),
                ActionType.REVIEW_READINGS_FOR_MONTH);

        boolean result = activityRepository.addUserAction(userAction);
        List<UserAction> activities = activityRepository.findAllUsersActions(Pageable.of(0, 100));
        UserAction testUserAction = activities.get(activities.size() - 1);

        assertAll(
                () -> assertThat(result).isTrue(),
                () -> assertThat(testUserAction.getUserId()).isEqualTo(userAction.getUserId()),
                () -> assertThat(testUserAction.getDateTime()).isEqualTo(userAction.getDateTime()),
                () -> assertThat(testUserAction.getAction()).isEqualTo(userAction.getAction())
        );
    }

    @Test
    @DisplayName("Returns activities list and verifies its elements")
    void getActivityList() {
        List<UserAction> userActivities = activityRepository.findAllUsersActions(Pageable.of(0, 10));

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