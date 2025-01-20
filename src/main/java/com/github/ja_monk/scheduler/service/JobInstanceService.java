package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        JobResDto jobResDto = jobService.findJob(jobInstReqDto.getName()); 
        String jobDefinition = jobResDto.getDefinition();

        // create job instance entry in DB
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobName(jobInstReqDto.getName());
        jobInstance.setScheduledTime(jobInstReqDto.getScheduledTime());
        jobInstance.setJobStatus(JobStatus.WAITING);
        
        jobInstRepo.save(jobInstance);
        
        if (jobInstReqDto.getScheduledTime().isBefore(LocalDateTime.now())) {
            jobRunner.runJob(jobInstance, jobDefinition);
        }

        JobInstResDto jobInstResDto = new JobInstResDto(jobInstance);
        return jobInstResDto;

        // TODO: restart job runner waiting for next job

    }
 

}
