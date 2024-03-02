package com.denmiagkov.meter.IT;

import com.denmiagkov.meter.IT.config.PostgresExtension;
import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.mapper.MeterReadingMapper;
import com.denmiagkov.meter.application.mapper.UserActionMapper;
import com.denmiagkov.meter.application.mapper.UserMapper;
import com.denmiagkov.meter.application.repository.ActivityRepository;
import com.denmiagkov.meter.application.repository.DictionaryRepository;
import com.denmiagkov.meter.application.repository.MeterReadingRepository;
import com.denmiagkov.meter.application.repository.UserRepository;
import com.denmiagkov.meter.application.service.exceptions.AdminNotAuthorizedException;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.domain.User;
import com.denmiagkov.meter.domain.UserAction;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgresExtension.class)
@DirtiesContext
class AdminControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeterReadingRepository meterReadingRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private DictionaryRepository dictionaryRepository;
    @Autowired
    private MeterReadingMapper meterReadingMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserActionMapper userActionMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setLogin("admin");
        loginRequest.setPassword("321");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/login")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseBody, JwtResponse.class);
        token = jwtResponse.getAccessToken();
    }

    @Test
    @DisplayName("Admin enters invalid password and method throwsException")
    void getAllUsers_ThrowsAdminNotAuthorizedException() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                        .param("page", "0")
                        .param("size", "5")
                        .header("Authorization", String.join(" ",
                                "Bearer", "dummy"))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpectAll(status().isUnauthorized(),
                        result -> assertThat(result.getResolvedException() instanceof AdminNotAuthorizedException));
    }

    @Test
    @DisplayName("Admin authorized and method returns all users set successfully")
    void getAllUsers_Successful() throws Exception {
        Set<User> users = userRepository.findAllUsers(Pageable.of(0, 5));
        Set<UserDto> expectedResultSet = userMapper.usersToUserDtos(users);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/admin/users")
                        .param("page", "0")
                        .param("size", "5")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var contentAsString = mvcResult.getResponse().getContentAsString();
        JsonNode jsonObjects = objectMapper.readValue(contentAsString, JsonNode.class);
        Set<UserDto> actualResultSet = objectMapper.convertValue(
                jsonObjects,
                new TypeReference<Set<UserDto>>() {
                }
        );
        assertAll(
                () -> assertThat(actualResultSet).hasSameClassAs(expectedResultSet),
                () -> assertThat(actualResultSet).hasSameSizeAs(expectedResultSet),
                () -> assertThat(actualResultSet).containsExactlyInAnyOrderElementsOf(expectedResultSet)
        );
    }

    @Test
    @DisplayName("Admin authorized and method returns all meter readings list ")
    void getAllMeterReadingsList() throws Exception {
        List<MeterReading> allMeterReadings = meterReadingRepository.findAllMeterReadings(
                Pageable.of(0, 10));
        List<MeterReadingDto> expectedResultList = meterReadingMapper.listMeterReadingToListMeterReadingDto(allMeterReadings);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/admin/readings")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        objectMapper.findAndRegisterModules();
        JsonNode jsonObjects = objectMapper.readValue(jsonResponse, JsonNode.class);
        List<MeterReadingDto> actualResultList = objectMapper.convertValue(
                jsonObjects,
                new TypeReference<List<MeterReadingDto>>() {
                }
        );

        assertThat(actualResultList).isEqualTo(expectedResultList);
    }

    @Test
    @DisplayName("Admin authorized and method returns all user actions list ")
    void getAllUsersActions() throws Exception {
        List<UserAction> allUserActions = activityRepository.findAllUsersActions(
                Pageable.of(0, 10));
        List<UserActionDto> expectedResultList = userActionMapper.userActionsToUserActionDtos(allUserActions);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/admin/actions")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        objectMapper.findAndRegisterModules();
        JsonNode jsonObjects = objectMapper.readValue(jsonResponse, JsonNode.class);
        List<UserActionDto> actualResultList = objectMapper.convertValue(
                jsonObjects,
                new TypeReference<List<UserActionDto>>() {
                }
        );

        assertThat(actualResultList).isEqualTo(expectedResultList);
    }

    @Test
    @DisplayName("New utility added to dictionary successfully, dictionary size increased")
    void addUtilityTypeToDictionary() throws Exception {
        int initialDictionarySize = dictionaryRepository.getAllUtilitiesTypes().size();
        Map<String, String> newUtility = new HashMap<>();
        newUtility.put("name", "electricity");
        String jsonRequest = objectMapper.writeValueAsString(newUtility);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/dictionary/new")
                        .content(jsonRequest)
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var contentAsString = mvcResult.getResponse().getContentAsString();
        var actualOutput = objectMapper.readValue(contentAsString, HashMap.class);
        Map<Integer, String> dictionary = dictionaryRepository.getAllUtilitiesTypes();
        int finalDictionarySize = dictionary.size();

        assertAll(
                () -> assertThat(dictionary).containsValue(
                        newUtility.get("name").toUpperCase()),
                () -> assertThat(finalDictionarySize).isGreaterThan(initialDictionarySize)
        );
    }

    @Test
    @DisplayName("Dictionary already contains utility that user intends to add, and method throws exception")
    void addUtilityTypeToDictionary_ThrowsPublicUtilityTypeAlreadyExistsException() throws Exception {
        Map<String, String> newUtility = new HashMap<>();
        newUtility.put("name", "heating");
        String jsonRequest = objectMapper.writeValueAsString(newUtility);

        mockMvc.perform(post("/api/v1/admin/dictionary/new")
                        .content(jsonRequest)
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isBadRequest(),
                        result -> assertThat(result.getResolvedException() instanceof PublicUtilityTypeAlreadyExistsException));
    }
}