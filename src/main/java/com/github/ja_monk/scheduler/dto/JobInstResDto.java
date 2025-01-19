package com.github.ja_monk.scheduler.dto;

import java.time.LocalDateTime;

import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.model.JobInstance;

public class JobInstResDto {
    private int id;
    private String name;
    private LocalDateTime nextRunTime;
    private JobStatus status;
    
    public JobInstResDto(JobInstance jobInstance) {
        this.id = jobInstance.getId();
        this.name = jobInstance.getJobName();
        this.nextRunTime = jobInstance.getNextRunTime();
        this.status = jobInstance.getJobStatus();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime nextRunTime() {
        return this.nextRunTime;
    }

    public JobStatus getStatus() {
        return this.status;
    }
}
