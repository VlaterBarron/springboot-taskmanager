package com.vlater.taskmanager.dto.request;

import java.time.LocalDate;

public class Filters {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean completed;

    public Filters(String title, LocalDate startDate, LocalDate endDate, Boolean completed) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
