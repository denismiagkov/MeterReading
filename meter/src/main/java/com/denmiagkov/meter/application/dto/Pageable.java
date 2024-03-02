package com.denmiagkov.meter.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Параметры пагинации при возвращении списка объектов
 */
@AllArgsConstructor
@Getter
public class Pageable {

    private int page;
    private int pageSize;

    public static Pageable of(int page, int pageSize) {
        return new Pageable(page, pageSize);
    }
}
