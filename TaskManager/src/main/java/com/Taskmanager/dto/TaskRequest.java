package com.Taskmanager.dto;

import java.time.LocalDate;

public class TaskRequest {

    private String tittle;
    private String description;
    private boolean completed;
    private LocalDate dueDate;

    //create default constructor
    public TaskRequest() {
    }


    //create getter & setter method

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
