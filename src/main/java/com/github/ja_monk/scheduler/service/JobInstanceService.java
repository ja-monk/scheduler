package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.github.ja_monk.scheduler.dto.JobInstReqDto;
import com.github.ja_monk.scheduler.dto.JobInstResDto;
import com.github.ja_monk.scheduler.dto.JobResDto;
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
    private JobRunnerService jobRunner;

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

        // TODO: Pointless??
        /*
        if (jobInstReqDto.getScheduledTime().isBefore(LocalDateTime.now())) {
            jobRunner.runJob(jobInstResDto);
        }
        
        // TODO: restart job runner waiting for next job
        */

        return jobInstResDto;
    }

    // TODO: Update, Delete, etc.
    //public JobInstResDto updateJobInstance(JobInstReqDto jobInstReqDto) {}

    // TODO: needed?
/*
    public ArrayList<JobInstResDto> findNextScheduledJobs() {
        ArrayList<JobInstance> jobInstList = jobInstRepo.findNextScheduledJobs();
        ArrayList<JobInstResDto> jobInstResDtoList = new ArrayList<>();
        for (JobInstance jobInst : jobInstList) {
            JobInstResDto jobInstResDto = new JobInstResDto(jobInst);
            jobInstResDtoList.add(jobInstResDto);
        }
        return jobInstResDtoList;
    } 
*/
}
