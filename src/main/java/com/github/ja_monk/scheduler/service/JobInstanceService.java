package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import com.github.ja_monk.scheduler.dto.JobInstReqDto;
import com.github.ja_monk.scheduler.dto.JobInstResDto;
import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;

@Service
public class JobInstanceService {
   @Autowired
   private JobInstanceRepository jobInstRepo;


    public JobInstResDto submitJob(JobInstReqDto jobInstReqDto) {
        JobInstance jobInstance = new JobInstance();
        jobInstance.setJobName(jobInstReqDto.getName());
        jobInstance.setScheduledTime(jobInstReqDto.getScheduledTime());
        jobInstance.setJobStatus(JobStatus.WAITING);
        
        jobInstRepo.save(jobInstance);
        /*
        // define the process builder to run the job 
        ProcessBuilder procBuilder = new ProcessBuilder(); 
        procBuilder.command(job.getDefinition());

        // capture stout & sterr
        procBuilder.redirectOutput(new File(job.getName() + "_stdout.log"));
        procBuilder.redirectError(new File(job.getName() + "_sterr.log"));

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
        */

        JobInstResDto jobInstResDto = new JobInstResDto(jobInstance);
        return jobInstResDto;
    }

   
   
}
