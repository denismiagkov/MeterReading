package com.denmiagkov.meter.application.dto.incoming;

import lombok.Getter;
import lombok.Setter;


@Setter
public class PaginationDto {

    /**
     * Параметр пагинации: размер страницы
     */
    @Getter
    private int pageSize;

    /**
     * Параметр пагинации: номер страницы
     */
    private int page;

    public int getPage() {
        return page * pageSize;
    }

}
