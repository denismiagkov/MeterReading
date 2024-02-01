package com.denmiagkov.meter.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Справочник типов показаний (услуг)
 */
public class PublicUtility {

    /**
     * Единственный экземпляр класса
     */
    public static final PublicUtility PUBLIC_UTILITY = new PublicUtility();
    /**
     * Перечень типов показаний
     */
    @Getter
    private final Map<Integer, String> utilityList = new HashMap<>();
    /**
     * Индекс следующего добавляемого типа показаний
     */
    private static Integer index = 4;

    /**
     * Конструктор
     */
    private PublicUtility() {
        utilityList.put(1, "HEATING");
        utilityList.put(2, "COLD_WATER");
        utilityList.put(3, "HOT_WATER");
    }

    /**
     * Метод добавления нового типа показаний (услуг)
     *
     * @param utility Новый тип показаний
     */
    public void addUtilityType(String utility) {
        utilityList.put(index++, utility);
    }

}
