package com.denmiagkov.meter.application.dto.incoming;

import com.denmiagkov.meter.domain.ActionType;

/**
 * Класс входящего ДТО для просмотра пользователем истории подачи показаний
 */
public class MeterReadingReviewHistoryDto extends IncomingDto {
    /**
     * id пользователя
     */
    private int userId;
    /**
     * Тип действия пользователя
     */
    private ActionType action;

    /**
     * Параметр пагинации: размер страницы
     */
    private int pageSize;

    /**
     * Параметр пагинации: номер страницы
     */
    private int page;

    /**
     * Конструктор, геттерф и сеттеры
     */
    public MeterReadingReviewHistoryDto() {
        this.action = ActionType.REVIEW_READINGS_HISTORY;
    }

    public Integer getUserId() {
        return userId;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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
