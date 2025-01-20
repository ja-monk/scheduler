package com.github.ja_monk.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;

@Service
public class JobRunnerService {
    // generic obbject to be used as lock for synchronization
    // when lock is held in by synchronized(lock) it needs to be released
    // before another method can run blokc with the same lock
    private final Object lock = new Object();
    private boolean restart = false;
    @Autowired
    private JobInstanceRepository jobInstRepo;

    // TODO: add method to find and wait for next job, synch(lock) & restart
    // if new job added with lock.wait(time) & restart trigger method

    
    public void runJob(JobInstance jobInstance, String definition) {
        // define the process builder to run the job 
        ProcessBuilder procBuilder = new ProcessBuilder(); 
        procBuilder.command("/bin/bash", "-c", definition);

        // capture stout & sterr
        procBuilder.redirectOutput(new File("logs/" + jobInstance.getJobName() + "_stdout.log"));
        procBuilder.redirectError(new File("logs/" + jobInstance.getJobName() + "_stderr.log"));

        // TODO: updated status as we start process
        jobInstance.setJobStatus(JobStatus.RUNNING);
        jobInstRepo.save(jobInstance);

        // run job
        try {
            Process jobRun = procBuilder.start();
            int exitCode = jobRun.waitFor();
            
            // update status based on error code
            if (exitCode == 0) {
                jobInstance.setJobStatus(JobStatus.COMPLETE);
            } else {
                jobInstance.setJobStatus(JobStatus.FAILED);
            }
        } catch (IOException e) {
            e.printStackTrace();    // update to overall app errors.log
            jobInstance.setJobStatus(JobStatus.FAILED);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO: set status

        }

        jobInstRepo.save(jobInstance); 

    }    

}
