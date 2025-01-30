package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.github.ja_monk.scheduler.dto.JobInstReqDto;
import com.github.ja_monk.scheduler.dto.JobInstResDto;
import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.enums.Enums.Repeat;
import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;

@Service
public class JobInstanceService {
    private static final Logger log = LoggerFactory.getLogger(JobInstanceService.class);
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
        if (jobInstReqDto.getRepeat() == null) {
            jobInstance.setRepeat(Repeat.N);
        } else {
            jobInstance.setRepeat(jobInstReqDto.getRepeat());
        } 


        jobInstRepo.save(jobInstance);
        
        JobInstResDto jobInstResDto = new JobInstResDto(jobInstance);
        
        log.info("New instance of " + jobInstResDto.getName() + " scheduled at " + 
                jobInstResDto.getScheduledTime() + " with repeat " + jobInstResDto.getRepeat());

        scheduler.recheckNextJob();

        return jobInstResDto;
    }

    // TODO: Update, Delete, etc.
    //public JobInstResDto updateJobInstance(JobInstReqDto jobInstReqDto) {}

}