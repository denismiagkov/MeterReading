package com.denmiagkov.meter.application.repository.impl;

import com.denmiagkov.meter.application.repository.DictionaryRepository;
import com.denmiagkov.meter.utils.ConnectionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс реализует логику взаимодействия с базой данных по поводу справочника услуг (типов показаний)
 */
@Repository
@AllArgsConstructor
public class DictionaryRepositoryImpl implements DictionaryRepository {

    private final ConnectionManager connectionManager;

    /**
     * SQL-запрос на добавление в справочник нового типа услуг
     */
    public static final String ADD_PUBLIC_UTILITY_TYPE_TO_DICTIONARY = """
            INSERT INTO meter_service.utilities_dictionary (utility_type)
            VALUES (?);
            """;

    /**
     * SQL-запрос на выборку всех типов услуг из справочника
     */
    private static final String GET_ALL_PUBLIC_UTILITIES_TYPES = """
            SELECT id, utility_type
            FROM meter_service.utilities_dictionary;
            """;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> addUtilityType(String utilityName) {
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(
                     ADD_PUBLIC_UTILITY_TYPE_TO_DICTIONARY,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, utilityName);
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            int newUtilityId = -1;
            if (generatedKey.next()) {
                newUtilityId = generatedKey.getInt("id");
            }
            Map<Integer, String> newUtility = new HashMap<>();
            newUtility.put(newUtilityId, utilityName);
            return newUtility;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, String> getAllUtilitiesTypes() {
        Map<Integer, String> allPublicUtilitiesTypes = new HashMap<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_PUBLIC_UTILITIES_TYPES)) {
            ResultSet queryResult = statement.executeQuery();
            while (queryResult.next()) {
                allPublicUtilitiesTypes.put(
                        queryResult.getInt("id"),
                        queryResult.getString("utility_type")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allPublicUtilitiesTypes;
    }
}
