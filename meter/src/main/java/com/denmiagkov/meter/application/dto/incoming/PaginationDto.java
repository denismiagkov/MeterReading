package com.denmiagkov.meter.application.dto.incoming;

public class PaginationDto {

    /**
     * Параметр пагинации: размер страницы
     */
    private int pageSize;

    /**
     * Параметр пагинации: номер страницы
     */
    private int page;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page * pageSize;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
