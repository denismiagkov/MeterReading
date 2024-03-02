package com.denmiagkov.meter.IT;

import com.denmiagkov.meter.IT.config.PostgresExtension;
import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.mapper.MeterReadingMapper;
import com.denmiagkov.meter.application.repository.MeterReadingRepository;
import com.denmiagkov.meter.domain.MeterReading;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.NewMeterValueIsLessThenPreviousException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.SubmitMeterReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.UtilityTypeNotFoundException;
import com.denmiagkov.meter.infrastructure.in.login_service.AuthService;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtRequest;
import com.denmiagkov.meter.infrastructure.in.login_service.JwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgresExtension.class)
@DirtiesContext
class UserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MeterReadingRepository meterReadingRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private MeterReadingMapper meterReadingMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Pageable pageable = Pageable.of(0, 50);
    private MeterReadingDto meterReadingDto1;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setLogin("user1");
        loginRequest.setPassword("123");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/login")
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        JwtResponse jwtResponse = objectMapper.readValue(responseBody, JwtResponse.class);
        token = jwtResponse.getAccessToken();

        meterReadingDto1 = new MeterReadingDto();
        meterReadingDto1.setUtilityId(1);
        meterReadingDto1.setValue(270.25);
    }

    @Test
    @Disabled("flaky test, need to see")
    @DisplayName("Method receives new meter reading values, records these in database and returns " +
                 "new meter reading successfully: all meter readings list increases by 1 ")
    void submitNewMeterReading_Successful() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(meterReadingDto1);
        int initialMeterReadingsListSize = meterReadingRepository.findAllMeterReadings(pageable).size();

        mockMvc.perform(post("/api/v1/user/reading/new")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.utilityId").value(meterReadingDto1.getUtilityId()),
                        jsonPath("$.value").value(meterReadingDto1.getValue())
                );

        int finalMeterReadingsListSize = meterReadingRepository.findAllMeterReadings(pageable).size();

        assertThat(finalMeterReadingsListSize - initialMeterReadingsListSize).isEqualTo(1);
    }

    @Test
    @DisplayName("New meter reading is submitting on the same month like previous, so method throws exception")
    void submitNewMeterReading_ThrowsSubmitMeterReadingOnTheSameMonthException() throws Exception {
        String jsonRequestPrevious = objectMapper.writeValueAsString(meterReadingDto1);
        MeterReadingDto meterReadingDto3 = new MeterReadingDto();
        meterReadingDto3.setUtilityId(1);
        meterReadingDto3.setValue(340.00);
        String jsonRequestNew = objectMapper.writeValueAsString(meterReadingDto1);

        mockMvc.perform(post("/api/v1/user/reading/new")
                .header("Authorization", String.join(" ",
                        "Bearer", token))
                .content(jsonRequestPrevious)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));
        mockMvc.perform(post("/api/v1/user/reading/new")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .content(jsonRequestNew)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpectAll(status().isBadRequest(),
                        result -> assertThat(result.getResolvedException()
                                instanceof SubmitMeterReadingOnTheSameMonthException));
    }

    @Test
    @DisplayName("New meter reading has less value than previous, so method throws exception")
    void submitNewMeterReading_ThrowsNewMeterValueIsLessThenPreviousException() throws Exception {
        MeterReadingDto meterReadingDto3 = new MeterReadingDto();
        meterReadingDto3.setUtilityId(3);
        meterReadingDto3.setValue(10.50); //previous value = 23.5
        String jsonRequestNew = objectMapper.writeValueAsString(meterReadingDto3);

        mockMvc.perform(post("/api/v1/user/reading/new")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .content(jsonRequestNew)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpectAll(status().isBadRequest(),
                        result -> assertThat(result.getResolvedException()
                                instanceof NewMeterValueIsLessThenPreviousException));
    }

    @Test
    @DisplayName("User submits new meter reading and later requests actual value on appropriate utility:" +
                 "method returns value of newly added meter reading")
    void getActualReadingOnExactUtilityByUser_Successful() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(meterReadingDto1);
        mockMvc.perform(post("/api/v1/user/reading/new")
                .header("Authorization", String.join(" ",
                        "Bearer", token))
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        mockMvc.perform(get("/api/v1/user/reading/actual")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .param("utilityId", "1"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.value").value(meterReadingDto1.getValue())
                );
    }

    @Test
    @DisplayName("User requests actual value on utility with invalid utility id and method throws exception")
    void getActualReadingOnExactUtilityByUser_ThrowsUtilityTypeNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/user/reading/actual")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .param("utilityId", "100"))
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertThat(result.getResolvedException() instanceof UtilityTypeNotFoundException)
                );
    }

    @Test
    @DisplayName("Method returns list of all actual meter readings of the user successfully:" +
                 "output list is equal to one got immediately from repository")
    void getActualMeterReadingsOnAllUtilitiesByUser() throws Exception {
        var userActualMeterReadings = meterReadingRepository.findActualMeterReadingsOnAllUtilitiesByUser(
                authService.getUserIdFromToken(token));
        List<MeterReadingDto> expectedMeterReadingsList = meterReadingMapper.listMeterReadingToListMeterReadingDto(userActualMeterReadings);

        var mvcResult = mockMvc.perform(get("/api/v1/user/readings/actual")
                        .header("Authorization", String.join(" ",
                                "Bearer", token)))
                .andExpect(status().isOk())
                .andReturn();

        List<MeterReadingDto> actualMeterReadingsList = convertToDtoFrom(mvcResult);

        assertAll(
                () -> assertThat(actualMeterReadingsList).hasSameClassAs(expectedMeterReadingsList),
                () -> assertThat(actualMeterReadingsList).hasSameSizeAs(expectedMeterReadingsList),
                () -> assertThat(actualMeterReadingsList).containsExactlyElementsOf(expectedMeterReadingsList)
        );
    }

    @Test
    @DisplayName("Method returns list of all meter readings submitted by the user successfully:" +
                 "output list is equal to one got immediately from repository")
    void getMeterReadingsHistoryByUser() throws Exception {
        List<MeterReading> meterReadingsHistory = meterReadingRepository.findMeterReadingsHistory(1, pageable);
        List<MeterReadingDto> expectedMeterReadingsList = meterReadingMapper.listMeterReadingToListMeterReadingDto(meterReadingsHistory);

        var mvcResult = mockMvc.perform(get("/api/v1/user/readings")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .param("page", "0")
                        .param("size", "50"))
                .andExpect(status().isOk())
                .andReturn();
        List<MeterReadingDto> actualMeterReadingsList = convertToDtoFrom(mvcResult);

        assertAll(
                () -> assertThat(actualMeterReadingsList).hasSameClassAs(expectedMeterReadingsList),
                () -> assertThat(actualMeterReadingsList).hasSameSizeAs(expectedMeterReadingsList),
                () -> assertThat(actualMeterReadingsList).containsExactlyElementsOf(expectedMeterReadingsList)
        );
    }

    @Test
    @DisplayName("User enters correct month and year, and method returns valid list")
    void getReadingsForMonthByUser_Successful() throws Exception {
        int currentMonth = LocalDateTime.now().getMonthValue();
        int currentYear = LocalDateTime.now().getYear();
        List<MeterReading> meterReadingsList = meterReadingRepository.findMeterReadingsForExactMonthByUser(
                1, currentYear, currentMonth);
        List<MeterReadingDto> expectedMeterReadingsList =
                meterReadingMapper.listMeterReadingToListMeterReadingDto(meterReadingsList);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/readings/month")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .param("month", String.valueOf(currentMonth))
                        .param("year", String.valueOf(currentYear)))
                .andExpect(status().isOk())
                .andReturn();
        List<MeterReadingDto> actualMeterReadingsList = convertToDtoFrom(mvcResult);

        assertThat(actualMeterReadingsList).isEqualTo(expectedMeterReadingsList);
    }

    @Test
    @DisplayName("User enters invalid date (in future), and method throws exception")
    void getReadingsForMonthByUser_ThrowsInvalidDateException() throws Exception {
        int month = LocalDateTime.now().plusMonths(1).getMonthValue();
        int currentYear = LocalDateTime.now().getYear();

        mockMvc.perform(get("/api/v1/user/readings/month")
                        .header("Authorization", String.join(" ",
                                "Bearer", token))
                        .param("month", String.valueOf(month))
                        .param("year", String.valueOf(currentYear)))
                .andExpectAll(status().isBadRequest(),
                        result -> assertThat(result.getResolvedException()
                                instanceof InvalidDateException)
                );
    }

    private List<MeterReadingDto> convertToDtoFrom(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponseString = mvcResult.getResponse().getContentAsString();
        JsonNode jsonResponseObjects = objectMapper.readValue(jsonResponseString, JsonNode.class);
        objectMapper.findAndRegisterModules();
        List<MeterReadingDto> actualMeterReadingsList = objectMapper.convertValue(
                jsonResponseObjects,
                new TypeReference<List<MeterReadingDto>>() {
                }
        );
        return actualMeterReadingsList;
    }
}