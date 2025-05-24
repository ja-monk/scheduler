package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        
        log.info("New instance of {} scheduled at {} with repeat {}", 
                jobInstResDto.getName(), jobInstResDto.getScheduledTime(), jobInstResDto.getRepeat());
        
        scheduler.recheckNextJob();

        return jobInstResDto;
    }

    public JobInstResDto cancelJob(int id) {
        //confirm job instance exists
        JobInstance jobInst;
        try {
            jobInst = jobInstRepo.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            // TODO: handle exception 
            throw e;
        }

        jobInst.setJobStatus(JobStatus.CANCELLED);
        jobInstRepo.save(jobInst);

        JobInstResDto jobInstResDto = new JobInstResDto(jobInst);

        log.info("Job {} (ID {}) CANCELLED", jobInstResDto.getName(), id);

        scheduler.recheckNextJob();
        
        return jobInstResDto;
    }

    public ArrayList<JobInstResDto> cancelAllJobs() {
        ArrayList<JobInstance> scheduledJobs = jobInstRepo.findAllScheduledJobs();
        ArrayList<JobInstResDto> cancelledJobs = new ArrayList<>();

        if (scheduledJobs.isEmpty()) {
            return cancelledJobs;
        }
        
        for (JobInstance jobInst : scheduledJobs) {
            jobInst.setJobStatus(JobStatus.CANCELLED);
            jobInstRepo.save(jobInst);
            JobInstResDto jobInstResDto = new JobInstResDto(jobInst);
            log.info("Job {} (ID {}) CANCELLED", jobInstResDto.getName(), jobInstResDto.getId());
            cancelledJobs.add(jobInstResDto);
        }

        scheduler.recheckNextJob();

        return cancelledJobs;
    }

    public ArrayList<JobInstResDto> getAllScheduledJobs() {
        ArrayList<JobInstance> scheduledJobInsts = jobInstRepo.findAllScheduledJobs();
        ArrayList<JobInstResDto> scheduledJobsRes = new ArrayList<>();

        if (scheduledJobInsts.isEmpty()) {
            return scheduledJobsRes;
        }

        for (JobInstance jobInst : scheduledJobInsts) {
            JobInstResDto jobInstResDto = new JobInstResDto(jobInst);
            scheduledJobsRes.add(jobInstResDto);
        }

        return scheduledJobsRes;
    }

    public JobInstResDto findJobInstance(int id) {
        // find job instance or throw excpetion if it doesnt exist
        JobInstance jobInst = jobInstRepo.findById(id).orElseThrow();
        JobInstResDto jobInstResDto = new JobInstResDto(jobInst);
        return jobInstResDto;
    }

    // TODO: Update, Delete, etc.
    //public JobInstResDto updateJobInstance(JobInstReqDto jobInstReqDto) {}

}