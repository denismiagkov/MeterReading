package com.denmiagkov.meter.infrastructure.in.controller;

import com.denmiagkov.meter.application.dto.Pageable;
import com.denmiagkov.meter.application.dto.outgoing.MeterReadingDto;
import com.denmiagkov.meter.application.dto.outgoing.UserActionDto;
import com.denmiagkov.meter.application.dto.outgoing.UserDto;
import com.denmiagkov.meter.application.mapper.MeterReadingMapper;
import com.denmiagkov.meter.application.mapper.UserActionMapper;
import com.denmiagkov.meter.application.mapper.UserMapper;
import com.denmiagkov.meter.application.service.DictionaryService;
import com.denmiagkov.meter.application.service.MeterReadingService;
import com.denmiagkov.meter.application.service.UserActivityService;
import com.denmiagkov.meter.application.service.UserService;
import com.denmiagkov.meter.domain.*;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.PublicUtilityTypeAlreadyExistsException;
import com.denmiagkov.meter.infrastructure.in.dto_handling.dtoValidator.validatorImpl.PublicUtilityValidatorImpl;
import com.denmiagkov.meter.infrastructure.in.exception_handling.handlers.GlobalExceptionHandler;
import com.denmiagkov.meter.domain.ActionType;
import com.denmiagkov.meter.domain.UserAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private MeterReadingService meterReadingService;
    @Mock
    private UserActivityService activityService;
    @Mock
    private DictionaryService dictionaryService;
    @Mock
    PublicUtilityValidatorImpl utilityValidator;
    @InjectMocks
    private AdminController adminController;
    ObjectMapper mapper = new ObjectMapper();
    private int page;
    private int size;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).setControllerAdvice(GlobalExceptionHandler.class).build();
        page = 0;
        size = 50;
    }

    @Test
    void getAllUsers() throws Exception {
        UserMapper userMapper = mock(UserMapper.class);
        User user1 = User.builder()
                .id(1)
                .name("Ivan")
                .phone("+7123456789")
                .address("Moscow")
                .role(UserRole.USER)
                .login("user1")
                .password("123")
                .build();
        UserDto userDto1 = mock(UserDto.class);
        User user2 = User.builder()
                .id(2)
                .name("Petr")
                .phone("+7123456788")
                .address("Vladivostok")
                .role(UserRole.USER)
                .login("user2")
                .password("456")
                .build();
        UserDto userDto2 = mock(UserDto.class);
        Set<UserDto> users = Set.of(userDto1, userDto2);
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/v1/admin/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$").isArray(),
                        jsonPath("$[0].id").isNumber()
                );
    }

    @Test
    @DisplayName("Method returns list of all meter readings successfully")
    void getAllMeterReadingsList() throws Exception {
        List<MeterReadingDto> meterReadings = List.of(mock(MeterReadingDto.class), mock(MeterReadingDto.class));
        when(meterReadingService.getAllMeterReadingsList(any(Pageable.class))).thenReturn(meterReadings);

        mockMvc.perform(get("/api/v1/admin/readings")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray()
                );
    }

    @Test
    @DisplayName("Method returns list of all users successfully")
    void getAllUsersActions() throws Exception {
        List<UserActionDto> userActions = mock(List.class);
        when(activityService.getUserActivitiesList(any(Pageable.class)))
                .thenReturn(anyList());

        mockMvc.perform(get("/api/v1/admin/actions")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray()
                );
    }

    @Test
    @DisplayName("Method receives name of new utility and adds it to dictionary successfully")
    void addUtilityTypeToDictionary_AddsNewUtility() throws Exception {
        String newUtility = "ELECTRICITY";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", newUtility);
        String requestJson = mapper.writeValueAsString(requestMap);
        when(utilityValidator.isValid(newUtility))
                .thenReturn(true);
        Map<Integer, String> dictionary = new HashMap<>();
        when(dictionaryService.addUtilityType(newUtility))
                .thenReturn(dictionary);

        mockMvc.perform(post("/api/v1/admin/dictionary/new/")
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    @DisplayName("Method receives name of utility already registered in dictionary and throws exception")
    void addUtilityTypeToDictionary_ThrowsException() throws Exception {
        String newUtility = "ELECTRICITY";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", newUtility);
        String requestJson = mapper.writeValueAsString(requestMap);
        when(utilityValidator.isValid(newUtility))
                .thenThrow(PublicUtilityTypeAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/admin/dictionary/new/")
                        .content(requestJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        result -> assertTrue(result.getResolvedException() instanceof PublicUtilityTypeAlreadyExistsException)
                );
    }
}