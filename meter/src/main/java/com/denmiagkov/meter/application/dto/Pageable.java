package com.denmiagkov.meter.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Pageable {
    int page;
    int pageSize;
}
