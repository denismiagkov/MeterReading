package com.denmiagkov.meter.application.service;

import com.denmiagkov.meter.application.repository.impl.DictionaryRepositoryImpl;
import com.denmiagkov.meter.application.service.impl.DictionaryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DictionaryServiceImplTest {
    @Mock
    DictionaryRepositoryImpl dictionaryRepository;
    @InjectMocks
    DictionaryServiceImpl dictionaryService;

    @Test
    @DisplayName("Method invokes appropriate method on dependent object")
    void addUtilityTypeToDictionary() {
        String utilityName = "ELECTRICITY";
        dictionaryService.addUtilityType(utilityName);
        verify(dictionaryRepository, times(1))
                .addUtilityType(utilityName);
    }

    @Test
    @DisplayName("Method invokes appropriate method on dependent object, and dependent object returns hashmap")
    void getUtilitiesDictionary() {
        Map<Integer, String> dictionary = mock(HashMap.class);
        when(dictionaryRepository.getAllUtilitiesTypes()).thenReturn(dictionary);

        Map<Integer, String> testDictionary = dictionaryService.getUtilitiesDictionary();

        assertThat(testDictionary).isEqualTo(dictionary);
    }
}
