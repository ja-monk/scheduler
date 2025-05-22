package com.github.ja_monk.scheduler.dto;

import java.time.LocalDateTime;

import com.github.ja_monk.scheduler.enums.Enums.Repeat;

public class JobInstReqDto {
    private String name;
    private LocalDateTime scheduledTime;  // can be converted from string format like 2025-01-01T13:45
    private Repeat repeat;
    //private int id;

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

    public Repeat getRepeat() {
        return this.repeat;
    }    

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    /*
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    */
}
