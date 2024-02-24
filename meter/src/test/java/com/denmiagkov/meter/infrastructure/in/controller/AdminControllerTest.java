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
    @Disabled
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
        Pageable pageable = Pageable.of(page, size);
        when(userService.getAllUsers(pageable)).thenReturn(users);

        mockMvc.perform(get("/api/v1/admin/users?page={page}&size={size}", page, size))
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
    @Disabled
    @DisplayName("Method returns list of all meter readings successfully")
    void getAllMeterReadingsList() throws Exception {
        MeterReading meterReading1 = new MeterReading(1, 1, LocalDateTime.now().minusDays(3), 2, 75.00);
        MeterReading meterReading2 = new MeterReading(2, 1, LocalDateTime.now(), 2, 231.4);
        MeterReadingMapper meterReadingMapper = mock(MeterReadingMapper.class);
      //  MeterReadingDto meterReadingDto1 = MeterReadingMapper.INSTANCE.meterReadingToMeterReadingDto(meterReading1);
        when(meterReadingMapper.meterReadingToMeterReadingDto(meterReading1)).thenReturn(any(MeterReadingDto.class));
        when(meterReadingMapper.meterReadingToMeterReadingDto(meterReading1)).thenReturn(any(MeterReadingDto.class));
      //  MeterReadingDto meterReadingDto2 = MeterReadingMapper.INSTANCE.meterReadingToMeterReadingDto(meterReading2);
        List<MeterReadingDto> meterReadings = List.of(mock(MeterReadingDto.class), mock(MeterReadingDto.class));
        Pageable pageable = Pageable.of(page, size);
        when(meterReadingService.getAllMeterReadingsList(pageable)).thenReturn(meterReadings);

        mockMvc.perform(get("/api/v1/admin/readings"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty(),
                        jsonPath("$[0].utilityId").value(meterReading1.getUtilityId()),
                        jsonPath("$[0].userId").isNumber(),
                        jsonPath("$[1].value").isNumber()
                );
    }

    @Test
    @Disabled
    @DisplayName("Method returns list of all users successfully")
    void getAllUsersActions() throws Exception {
        List<UserActionDto> userActions = mock(List.class);
        Pageable pageable = Pageable.of(page, size);
        when(activityService.getUserActivitiesList(pageable)).thenReturn(userActions);

        mockMvc.perform(get("/api/v1/admin/actions"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").isArray(),
                        jsonPath("$").isNotEmpty()
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