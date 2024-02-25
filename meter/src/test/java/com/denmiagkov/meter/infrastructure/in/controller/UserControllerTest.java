package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.incoming.ReviewActualMeterReadingDto;
import com.denmiagkov.meter.application.dto.incoming.ReviewMeterReadingForMonthDto;
import com.denmiagkov.meter.application.dto.incoming.SubmitNewMeterReadingDto;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.InvalidDateException;
import com.denmiagkov.meter.infrastructure.in.dto_handling.IncomingDtoBuilder;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.SubmitMeterReadingOnTheSameMonthException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.UtilityTypeNotFoundException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.handlers.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private MeterReadingService meterReadingService;
    @Mock
    private IncomingDtoBuilder dtoHandler;
    @InjectMocks
    UserController userController;
    ObjectMapper mapper = new ObjectMapper();
    MeterReadingDto meterReading1, meterReading2;
    String token;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(GlobalExceptionHandler.class).build();
        meterReading1 = new MeterReadingDto();
        meterReading1.setUserId(1);
        meterReading1.setDate(LocalDateTime.now().minusDays(7));
        meterReading1.setUtilityId(1);
        meterReading1.setValue(27.25);
        meterReading2 = new MeterReadingDto();
        meterReading2.setUserId(1);
        meterReading2.setDate(LocalDateTime.now());
        meterReading2.setUtilityId(2);
        meterReading2.setValue(112.00);
        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYXJpYSIsImV4cCI6MTcwODI3MzkyMywidXNlcklkIjo0MCwicm9sZSI6IlVTRVIifQ." +
                "nbC6vK6H6doFzev5Q9D_AXa7LKpVKxC2mh_Z8kyqQZj3pCf7C9uHHfG_LaHyVdFWG8oW9ExdodLMML7RBiIN_w";
    }

    @Test
    @DisplayName("Method receives correct request for submitting meter reading and returns new meter reading")
    void submitNewMeterReading_RightWork() throws Exception {
        SubmitNewMeterReadingDto requestDto = new SubmitNewMeterReadingDto();
        requestDto.setUtilityId(3);
        requestDto.setValue(1034.00);
        String requestJson = mapper.writeValueAsString(requestDto);
        doNothing().when(dtoHandler).updateSubmitNewMeterReadingDto(
                any(SubmitNewMeterReadingDto.class), eq(token));
        when(meterReadingService.submitNewMeterReading(any(SubmitNewMeterReadingDto.class)))
                .thenReturn(meterReading2);

        mockMvc.perform(post("/api/v1/user/reading/new")
                        .requestAttr("token", token)
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Method receives request for submitting meter reading second time on the same month and throws exception")
    void submitNewMeterReading_ThrowsException() throws Exception {
        SubmitNewMeterReadingDto requestDto = new SubmitNewMeterReadingDto();
        requestDto.setUtilityId(3);
        requestDto.setValue(1034.00);
        String requestJson = mapper.writeValueAsString(requestDto);
        doThrow(SubmitMeterReadingOnTheSameMonthException.class)
                .when(dtoHandler).updateSubmitNewMeterReadingDto(
                        any(SubmitNewMeterReadingDto.class), eq(token));

        mockMvc.perform(post("/api/v1/user/reading/new")
                        .requestAttr("token", token)
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof SubmitMeterReadingOnTheSameMonthException)
                );
    }

    @Test
    @DisplayName("Method receives correct utility id and returns actual meter reading on appropriate utility")
    void getActualReadingOnExactUtilityByUser() throws Exception {
        ReviewActualMeterReadingDto requestDto = mock(ReviewActualMeterReadingDto.class);
        when(dtoHandler.createReviewMeterReadingOnConcreteUtilityDto(anyInt(), anyString()))
                .thenReturn(requestDto);
        when(meterReadingService.getActualMeterReadingOnExactUtilityByUser(requestDto))
                .thenReturn(meterReading1);

        mockMvc.perform(get("/api/v1/user/reading/actual")
                        .requestAttr("token", token)
                        .param("utilityId", "1"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$.utilityId").value(meterReading1.getUtilityId()),
                        jsonPath("$.userId").value(meterReading1.getUserId()),
                        jsonPath("$.value").value(meterReading1.getValue())
                );
    }

    @Test
    @DisplayName("Method receives incorrect utility id and throws exception")
    void getActualReadingOnExactUtilityByUser_ThrowsException() throws Exception {
        ReviewActualMeterReadingDto requestDto = mock(ReviewActualMeterReadingDto.class);
        when(dtoHandler.createReviewMeterReadingOnConcreteUtilityDto(anyInt(), anyString()))
                .thenThrow(UtilityTypeNotFoundException.class);

        mockMvc.perform(get("/api/v1/user/reading/actual")
                        .requestAttr("token", token)
                        .param("utilityId", "200"))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        result -> assertTrue(result.getResolvedException() instanceof UtilityTypeNotFoundException)
                );
    }

    @Test
    @DisplayName("Method receives request and sends response with actual meter readings of the user successfully")
    void getActualMeterReadingsOnAllUtilitiesByUser() throws Exception {
        ReviewActualMeterReadingDto requestDto = mock(ReviewActualMeterReadingDto.class);
        when(dtoHandler.createReviewAllActualMeterReadingsDto(anyString()))
                .thenReturn(requestDto);
        List<MeterReadingDto> actualMeterReadingsOnAllUtilities = List.of(meterReading1, meterReading2);
        when(meterReadingService.getActualMeterReadingsOnAllUtilitiesByUser(requestDto))
                .thenReturn(actualMeterReadingsOnAllUtilities);

        mockMvc.perform(get("/api/v1/user/readings/actual")
                        .requestAttr("token", token))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$[0].utilityId").value(meterReading1.getUtilityId()),
                        jsonPath("$[0].userId").value(meterReading1.getUserId())
                );
    }

    @Test
    @DisplayName("Method receives correct month and year and returns meter readings list for this period successfully")
    void getReadingsForMonthByUser() throws Exception {
        ReviewMeterReadingForMonthDto requestDto = mock(ReviewMeterReadingForMonthDto.class);
        when(dtoHandler.createReviewMeterReadingsForMonthDto(anyInt(), anyInt(), eq(token)))
                .thenReturn(requestDto);
        List<MeterReadingDto> meterReadingsForSelectedMonth = List.of(meterReading1, meterReading2);
        when(meterReadingService.getReadingsForMonthByUser(requestDto))
                .thenReturn(meterReadingsForSelectedMonth);

        mockMvc.perform(get("/api/v1/user/readings/month")
                        .requestAttr("token", token)
                        .param("month", "2")
                        .param("year", "2024"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$[0].userId").value(meterReading1.getUserId())
                );
    }

    @Test
    @DisplayName("Method receives year in future (invalid data) and throws exception")
    void getReadingsForMonthByUser_ThrowsException() throws Exception {
        when(dtoHandler.createReviewMeterReadingsForMonthDto(anyInt(), anyInt(), eq(token)))
                .thenThrow(InvalidDateException.class);

        mockMvc.perform(get("/api/v1/user/readings/month")
                        .requestAttr("token", token)
                        .param("month", "2")
                        .param("year", "2025"))
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof InvalidDateException)
                );
    }
}