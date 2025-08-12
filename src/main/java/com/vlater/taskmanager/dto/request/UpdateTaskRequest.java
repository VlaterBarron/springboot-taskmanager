package com.vlater.taskmanager.dto.request;

import jakarta.validation.constraints.Size;

import java.util.Date;

public class UpdateTaskRequest {

    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    private Date dueDate;
    private boolean completed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
