package com.github.ja_monk.scheduler.model;

import java.time.LocalDateTime;

import com.github.ja_monk.scheduler.enums.Enums.JobStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_instances")
public class JobInstance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_instance_seq_gen")
    @SequenceGenerator(name = "job_instance_seq_gen", sequenceName = "job_instance_seq", allocationSize = 1)
    private int id;
    private String jobName;
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)    // store string value of status in DB
    private JobStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public JobInstance() {}

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String name) {
        this.jobName = name;
    }

    public LocalDateTime getScheduledTime() {
        return this.scheduledTime;
    }

    public void setScheduledTime(LocalDateTime time) {
        this.scheduledTime = time;
    }

    public JobStatus getJobStatus() {
        return this.status;
    }

    public void setJobStatus(JobStatus status) {
        this.status = status;
    }

    public int getId() {    // only need getter as id set by DB
        return this.id;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime starTime) {
        this.startTime = starTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
