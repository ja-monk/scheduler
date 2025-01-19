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
    private LocalDateTime nextRunTime;
    @Enumerated(EnumType.STRING)    // store string value of status in DB
    private JobStatus status;

    public JobInstance() {}

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String name) {
        this.jobName = name;
    }

    public LocalDateTime getNextRunTime() {
        return this.nextRunTime;
    }

    public void setNextRunTime(LocalDateTime time) {
        this.nextRunTime = time;
    }

    public JobStatus getJobStatus() {
        return this.status;
    }

    public void setJobStatus(JobStatus status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

}
