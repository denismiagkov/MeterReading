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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class DictionaryRepositoryImplTest {

    private static final int CONTAINER_PORT = 5432;
    private static final int LOCAL_PORT = 5431;
    DictionaryRepository dictionaryRepository;
    Connection connection;

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
        dictionaryRepository = new DictionaryRepositoryImpl();
        connection = ConnectionManager.open();
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    @DisplayName("Number of records increases on 1 after adding another record, and utilityToAdd equals addedUtility")
    void addUtilityTypeToDictionary() {
        String utilityToAdd = "ELECTRICITY";
        int listSizeBefore = dictionaryRepository.getAllUtilitiesTypes().size();
        int utilityId = dictionaryRepository.addUtilityTypeToDictionary(utilityToAdd);
        int listSizeAfter = dictionaryRepository.getAllUtilitiesTypes().size();

        String addedUtility = dictionaryRepository.getAllUtilitiesTypes().get(utilityId);

        assertAll(
                () -> assertThat(listSizeAfter).isGreaterThan(listSizeBefore),
                () -> assertThat(addedUtility).isEqualTo(utilityToAdd)
        );
    }

    @Test
    @DisplayName("Method returns map of exactly three public utilities recorded by migration")
    void getAllUtilitiesTypes() {
        Map<Integer, String> utilities = dictionaryRepository.getAllUtilitiesTypes();

        assertThat(utilities).hasSize(3)
                .containsValues("HEATING", "HOT_WATER", "COLD_WATER");
    }
}