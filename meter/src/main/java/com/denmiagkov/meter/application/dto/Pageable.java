package com.denmiagkov.meter.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Параметры пагинации при возвращении списка объектов
 */
@AllArgsConstructor(staticName = "of")
@Getter
public class Pageable {

    private int page;
    private int pageSize;
}
