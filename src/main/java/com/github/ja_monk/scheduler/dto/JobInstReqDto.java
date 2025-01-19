package com.github.ja_monk.scheduler.dto;

import java.time.LocalDateTime;

public class JobInstReqDto {
    private String name;
    private LocalDateTime nextRunTime;

    public String getName() {
        return this.name;
    } 

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getNextRunTime() {
        return this.nextRunTime;
    }

    public void setNextRunTime(LocalDateTime time) {
        this.nextRunTime = time;
    }
}
