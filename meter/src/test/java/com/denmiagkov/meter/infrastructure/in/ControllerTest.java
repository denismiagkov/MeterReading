package com.denmiagkov.meter.infrastructure.in;

import com.denmiagkov.meter.application.service.*;
import com.denmiagkov.meter.infrastructure.in.controller.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
    @Mock
    DictionaryServiceImpl dictionaryService;
    @InjectMocks
    Controller controller;

    @Test
    @DisplayName("Method invokes appropriate method on dependent object")
    void addUtilityType() {
        String utilityName = "ELECTRICITY";
        controller.addUtilityTypeToDictionary(utilityName);

        verify(dictionaryService, times(1))
                .addUtilityTypeToDictionary(utilityName);
    }
}