package com.denmiagkov.meter.application.dto.incoming;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationDto {

    /**
     * Параметр пагинации: размер страницы
     */
    private int pageSize;

    /**
     * Параметр пагинации: номер страницы
     */
    private int page;

}
