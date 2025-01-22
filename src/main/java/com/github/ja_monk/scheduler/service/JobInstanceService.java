package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import com.github.ja_monk.scheduler.dto.JobInstReqDto;
import com.github.ja_monk.scheduler.dto.JobInstResDto;
import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;

@Service
public class JobInstanceService {
    @Autowired
    private JobInstanceRepository jobInstRepo;
    @Autowired
    private JobService jobService;
    @Autowired
    private SchedulingService scheduler;

    public JobInstResDto submitJob(JobInstReqDto jobInstReqDto) {
        // check job exists
        try {
            jobService.findJob(jobInstReqDto.getName()); 
        } catch (NoSuchElementException e) {
            // TODO: how to handle better
            throw e;    
        }

        // create job instance entry in DB
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobName(jobInstReqDto.getName());
        jobInstance.setScheduledTime(jobInstReqDto.getScheduledTime());
        jobInstance.setJobStatus(JobStatus.WAITING);
        
        jobInstRepo.save(jobInstance);
        
        JobInstResDto jobInstResDto = new JobInstResDto(jobInstance);
 
        scheduler.recheckNextJob();

        return jobInstResDto;
    }

    // TODO: Update, Delete, etc.
    //public JobInstResDto updateJobInstance(JobInstReqDto jobInstReqDto) {}

}