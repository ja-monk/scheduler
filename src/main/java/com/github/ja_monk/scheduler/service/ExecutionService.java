package com.github.ja_monk.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import com.github.ja_monk.scheduler.dto.JobResDto;
import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;

@Service
public class ExecutionService {
    private static final Logger log = LoggerFactory.getLogger(ExecutionService.class);

    @Autowired
    private JobService jobService;
    @Autowired
    private JobInstanceRepository jobInstRepo;
    
    @Async
    public void runJob(JobInstance jobInstance) { 
        // update status as we start process
        // TODO: Better here but not quick enough, multiple versions of job kicked off 
        // jobInstance.setJobStatus(JobStatus.RUNNING);
        // jobInstRepo.save(jobInstance);
        
        log.info("Executing job: " + jobInstance.getJobName());
        
        // get job definition
        JobResDto jobResDto = jobService.findJob(jobInstance.getJobName()); 
        String jobDefinition = jobResDto.getDefinition();

        // define the process builder to run the job 
        ProcessBuilder procBuilder = new ProcessBuilder(); 
        procBuilder.command("/bin/bash", "-c", jobDefinition);

        // capture stout & sterr
        procBuilder.redirectOutput(new File("logs/" + jobInstance.getJobName() + "_stdout.log"));
        procBuilder.redirectError(new File("logs/" + jobInstance.getJobName() + "_stderr.log"));

        // set start time & run job
        jobInstance.setStartTime(LocalDateTime.now());
        jobInstRepo.save(jobInstance);
        try {
            Process jobRun = procBuilder.start();
            int exitCode = jobRun.waitFor();
            
            jobInstance.setEndTime(LocalDateTime.now());
            // update status based on error code
            if (exitCode == 0) {
                jobInstance.setJobStatus(JobStatus.COMPLETE);
                log.info(jobInstance.getJobName() + " complete.");
            } else {
                jobInstance.setJobStatus(JobStatus.FAILED);
            }
        } catch (IOException e) {
            e.printStackTrace();    // update to overall app errors.log
            jobInstance.setJobStatus(JobStatus.FAILED);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO: set status
            jobInstance.setJobStatus(JobStatus.FAILED);
        }

        jobInstRepo.save(jobInstance); 
    }    
}
