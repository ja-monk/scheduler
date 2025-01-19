package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
        
        // TODO: run if sched time is now
        runJob(jobInstance, jobDefinition); 


        JobInstResDto jobInstResDto = new JobInstResDto(jobInstance);
        return jobInstResDto;

    }
 
    public void runJob(JobInstance jobInstance, String definition) {
        // define the process builder to run the job 
        ProcessBuilder procBuilder = new ProcessBuilder(); 
        procBuilder.command("/bin/bash", "-c", definition);

        // capture stout & sterr
        procBuilder.redirectOutput(new File("logs/" + jobInstance.getJobName() + "_stdout.log"));
        procBuilder.redirectError(new File("logs/" + jobInstance.getJobName() + "_stderr.log"));

        // TODO: updated status as we start process

        // run job
        try {
            Process jobRun = procBuilder.start();
            int exitCode = jobRun.waitFor();
            
            // TODO: update status based on error code           
            
        } catch (IOException e) {
            e.printStackTrace();    // update to overall app errors.log 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }    


}
