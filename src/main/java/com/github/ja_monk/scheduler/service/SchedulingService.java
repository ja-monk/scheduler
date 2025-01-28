package com.github.ja_monk.scheduler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;


@Service
public class SchedulingService {
    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);
    // generic obbject to be used as lock for synchronization
    // when lock is held in by synchronized(lock) it needs to be released
    // before another method can run block with the same lock
    private final Object lock = new Object();
    private boolean restart;
    @Autowired
    private JobInstanceRepository jobInstRepo;
    @Autowired
    private ExecutionService executor;

    @Async          // run this method in a separate thread (otherwise rest of app is blocked by the wait)
    public void scheduleJobs() throws InterruptedException {    // TODO: Handle interupted exception?
        log.info("SchedulingService started successfully.");     
        // Start while true loop as we want job runner to always be running
        while (true) {            
            restart = false;
            // synchronised with method that restarts the loop
            // any time a job is added/updated/etc, we restart the loop and check for next job to run
            synchronized (lock) {
                log.info("Looking for next scheduled job(s).");
                // find all jobs which have status waiting and have minimum scheduled time
                ArrayList<JobInstance> nextJobsList = jobInstRepo.findNextScheduledJobs();

                // if no jobs scheduled and waiting, then wait for a new job
                if (nextJobsList.isEmpty()) {
                    log.info("No jobs found - entering wait."); 
                    lock.wait();
                    continue;   // wait is interupted when notified, we want to redo the check for jobs to run
                }
                
                LocalDateTime scheduledTime = nextJobsList.get(0).getScheduledTime();

                // if time is in the past run all jobs restart loop to check for jobs with next minimum time
                if (scheduledTime.isBefore(LocalDateTime.now())) {
                    log.info("Identified jobs scheduled in the past - running these jobs.");
                    for (JobInstance jobInstance : nextJobsList) {
                        jobInstance.setJobStatus(JobStatus.RUNNING);
                        jobInstRepo.save(jobInstance);
                        executor.runJob(jobInstance);
                    }
                    continue;
                }

                log.info("Next job(s) scheduled at " + nextJobsList.get(0).getScheduledTime());
                
                // for jobs scheduled in future, find time between now and scheduled time, absolute time to handle edge case
                Long untilScheduledTime = Duration.between(LocalDateTime.now(), scheduledTime).abs().toMillis();

                // wait from now until scheduled time
                lock.wait(untilScheduledTime);

                // if we have been notified this means we want to redo check for jobs 
                // this variable should then be true
                if (restart == true) {
                    log.info("Change to scheduled jobs detected.");
                    continue;
                }
                
                // if we came out of wait naturally, then time should now be scheduled time so we run jobs
                // TODO: run jobs multithreaded 
                for (JobInstance jobInstance : nextJobsList) {
                    log.info("Running jobs");
                    jobInstance.setJobStatus(JobStatus.RUNNING);
                    jobInstRepo.save(jobInstance);
                    executor.runJob(jobInstance);
                }
            }
        }
    }

    public void recheckNextJob() {
        synchronized(lock) {
            // set restart to true so jobRunner restarts
            restart = true;
            lock.notify();
        }
    }

}
