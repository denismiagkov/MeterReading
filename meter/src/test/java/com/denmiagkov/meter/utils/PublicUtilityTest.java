package com.denmiagkov.meter.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * */
class PublicUtilityTest {

    @Test
    void getUtilityList() {
        Map<Integer, String> utilityTypes = PublicUtility.PUBLIC_UTILITY.getUtilityList();

        assertThat(utilityTypes)
                .isNotNull()
                .hasSize(3);
    }

    @Test
    void addUtilityType() {
        PublicUtility.PUBLIC_UTILITY.addUtilityType("ELECTRICITY");
        Map<Integer, String> modifiedUtilityTypes = PublicUtility.PUBLIC_UTILITY.getUtilityList();
        String newValue = modifiedUtilityTypes.get(4);

        assertAll(
                () -> assertThat(modifiedUtilityTypes).hasSize(4),
                () -> assertThat(newValue).isEqualTo("ELECTRICITY")
        );
    }


}