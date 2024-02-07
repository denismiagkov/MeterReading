package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.utils.ConnectionManager;
import com.denmiagkov.meter.utils.LiquibaseManager;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class MeterReadingRepositoryImplTest {

    private static final int CONTAINER_PORT = 5432;
    private static final int LOCAL_PORT = 5431;
    MeterReadingRepositoryImpl meterReadingRepository;
    UserRepositoryImpl userRepository;
    Connection connection;
    User testUser;
    MeterReading testMeterReading;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("meter")
            .withUsername("meter")
            .withPassword("123")
            .withExposedPorts(CONTAINER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig()
                            .withPortBindings(new PortBinding(Ports.Binding.bindPort(LOCAL_PORT), new ExposedPort(CONTAINER_PORT)))
            ));

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() throws SQLException {
        LiquibaseManager.startLiquibase();
        userRepository = new UserRepositoryImpl();
        meterReadingRepository = new MeterReadingRepositoryImpl();
        connection = ConnectionManager.open();
        connection.setAutoCommit(false);

        testUser = userRepository.authenticateUser("user1", "123");
        testMeterReading = MeterReading.builder()
                .userId(testUser.getId())
                .date(LocalDateTime.of(2024, 2, 4, 16, 41))
                .value(715.75)
                .utilityId(1)
                .build();

    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    @DisplayName("Number of records increases on 1 after adding another record")
    void addNewMeterReading() {
        int listSizeBefore = meterReadingRepository.getAllMeterReadings().size();
        meterReadingRepository.addNewMeterReading(testMeterReading);
        int listSizeAfter = meterReadingRepository.getAllMeterReadings().size();
        MeterReading meterReading = meterReadingRepository.getActualMeterReadingOnExactUtility(testUser, 1);

        assertAll(
                () -> assertThat(listSizeAfter).isGreaterThan(listSizeBefore),
                () -> assertThat(meterReading.getValue()).isEqualTo(715.75)
        );
    }


    @Test
    @DisplayName("Returns non-empty meter readings list of only one user")
    void getActualMeterReadingsOnAllUtilitiesByUser() {
        List<MeterReading> testUserActualMeterReadings =
                meterReadingRepository.getActualMeterReadingsOnAllUtilitiesByUser(testUser);

        List<MeterReading> notTestUserMeterReadings = testUserActualMeterReadings.stream()
                .filter(e -> e.getUserId() != testUser.getId())
                .toList();
        assertAll(
                () -> assertThat(testUserActualMeterReadings).isNotEmpty(),
                () -> assertThat(notTestUserMeterReadings).isEmpty()
        );
    }

    @Test
    @DisplayName("Returns last added meter reading")
    void getActualMeterReadingOnExactUtility() {
        MeterReading previousActualMeterReading = meterReadingRepository.getActualMeterReadingOnExactUtility(testUser, 1);
        meterReadingRepository.addNewMeterReading(testMeterReading);
        MeterReading newActualMeterReading = meterReadingRepository.getActualMeterReadingOnExactUtility(testUser, 1);

        assertAll(
                () -> assertThat(newActualMeterReading)
                        .isNotEqualTo(previousActualMeterReading),
                () -> assertThat(newActualMeterReading.getDate())
                        .isAfter(previousActualMeterReading.getDate()),
                () -> assertThat(newActualMeterReading.getDate()).isEqualTo(testMeterReading.getDate())
        );
    }

    @Test
    @DisplayName("Returns non-empty list of all users meter readings")
    void getAllMeterReadings() {
        List<MeterReading> allMeterReadingList = meterReadingRepository.getAllMeterReadings();
        List<MeterReading> testUserReadingsHistory = allMeterReadingList.stream()
                .filter(e -> e.getUserId() == testUser.getId())
                .toList();

        assertThat(allMeterReadingList)
                .hasSizeGreaterThan(testUserReadingsHistory.size())
                .hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("Returns non-empty list of only one user meter readings")
    void getMeterReadingsHistory() {
        List<MeterReading> meterReadingHistory = meterReadingRepository.getMeterReadingsHistory(testUser);
        List<MeterReading> testUserReadingsHistory = meterReadingHistory.stream()
                .filter(e -> e.getUserId() == testUser.getId())
                .toList();

        assertThat(meterReadingHistory)
                .hasSameSizeAs(testUserReadingsHistory)
                .hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("Returns meter reading list of December of user testUser")
    void getMeterReadingsForExactMonthByUser() {
        List<MeterReading> decemberTestUserMeterReadings =
                meterReadingRepository.getMeterReadingsForExactMonthByUser(testUser, 2023, 12);

        int meterReadingsCount = decemberTestUserMeterReadings.size();
        List<MeterReading> filteredListByUser = decemberTestUserMeterReadings.stream()
                .filter(e -> e.getUserId() == testUser.getId())
                .toList();
        Month month = decemberTestUserMeterReadings.get(0).getDate().getMonth();

        assertAll(
                () -> assertThat(meterReadingsCount).isEqualTo(3),
                () -> assertThat(decemberTestUserMeterReadings).hasSameSizeAs(filteredListByUser),
                () -> assertThat(month).isEqualTo(Month.DECEMBER)
        );
    }
}