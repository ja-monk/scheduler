package com.github.ja_monk.scheduler.dto;

import java.time.LocalDateTime;

import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.enums.Enums.Repeat;
import com.github.ja_monk.scheduler.model.JobInstance;

public class JobInstResDto {
    private int id;
    private String name;
    private LocalDateTime scheduledTime;
    private JobStatus status;
    private Repeat repeat;
    
    public JobInstResDto(JobInstance jobInstance) {
        this.id = jobInstance.getId();
        this.name = jobInstance.getJobName();
        this.scheduledTime = jobInstance.getScheduledTime();
        this.status = jobInstance.getJobStatus();
        this.repeat = jobInstance.getRepeat();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime getScheduledTime() {
        return this.scheduledTime;
    }

    public JobStatus getStatus() {
        return this.status;
    }

    public Repeat getRepeat() {
        return this.repeat;
    }

}
