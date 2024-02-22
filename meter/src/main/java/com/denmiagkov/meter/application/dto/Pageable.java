package com.denmiagkov.meter.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pageable {
    int page;
    int pageSize;

    public static Pageable of(int page, int pageSize) {
        return new Pageable(page, pageSize);
    }
}
