package com.denmiagkov.meter.application.repository;

import com.denmiagkov.meter.application.repository.impl.DictionaryRepositoryImpl;
import com.denmiagkov.meter.utils.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class DictionaryRepositoryImplTest {
    DictionaryRepository dictionaryRepository;
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
    @DisplayName("Number of records increases on 1 after adding another record")
    void addUtilityTypeToDictionary() {
        String utilityToAdd = "ELECTRICITY";
        int listSizeBefore = dictionaryRepository.getAllUtilitiesTypes().size();
        Map<Integer, String> newUtility = dictionaryRepository.addUtilityType(utilityToAdd);
        int listSizeAfter = dictionaryRepository.getAllUtilitiesTypes().size();

        assertThat(listSizeAfter).isGreaterThan(listSizeBefore);
    }

    @Test
    @DisplayName("Method returns map of exactly three public utilities recorded by migration")
    void getAllUtilitiesTypes() {
        Map<Integer, String> utilities = dictionaryRepository.getAllUtilitiesTypes();

        assertThat(utilities).hasSize(3)
                .containsValues("HEATING", "HOT_WATER", "COLD_WATER");
    }
}