package com.github.ja_monk.scheduler.dto;

import java.time.LocalDateTime;

public class JobInstReqDto {
    private String name;
    private LocalDateTime scheduledTime;  // can be converted from string format like 2025-01-01T13:45

    public String getName() {
        return this.name;
    } 

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getScheduledTime() {
        return this.scheduledTime;
    }

    public void setScheduledTime(LocalDateTime time) {
        this.scheduledTime = time;
    }
}
