package com.denmiagkov.meter.infrastructure.in.dto_handling;

import com.denmiagkov.meter.application.dto.Pageable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PageableCreator {
    public static Pageable createPageable(int page, int pageSize) {
        return Pageable.builder()
                .page(page)
                .pageSize(pageSize)
                .build();
    }
}
