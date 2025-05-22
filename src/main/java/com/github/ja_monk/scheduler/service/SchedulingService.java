package com.github.ja_monk.scheduler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.ja_monk.scheduler.enums.Enums.JobStatus;
import com.github.ja_monk.scheduler.enums.Enums.Repeat;
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
    // initialise and define repeat hashmap, items added using static block 
    private static HashMap<String, TemporalAmount> repeatDuration = new HashMap<>();
    
    static {
        repeatDuration.put(Repeat.H.toString(), Duration.ofHours(1));
        repeatDuration.put(Repeat.D.toString(), Period.ofDays(1));
        repeatDuration.put(Repeat.W.toString(), Period.ofWeeks(1));
        repeatDuration.put(Repeat.M.toString(), Period.ofMonths(1));
        repeatDuration.put(Repeat.Y.toString(), Period.ofYears(1));
    }


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
                        executeJob(jobInstance);

                        if (jobInstance.getRepeat() != Repeat.N) {
                            submitRepeatJobs(jobInstance, scheduledTime);
                        } 
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
                for (JobInstance jobInstance : nextJobsList) {
                    log.info("Running jobs");

                    executeJob(jobInstance);

                    if (jobInstance.getRepeat() != Repeat.N) {
                        submitRepeatJobs(jobInstance, scheduledTime);
                    } 
                }
            }
        }
    }

    public void submitRepeatJobs(JobInstance currJobInstance, LocalDateTime scheduledTime) {
        LocalDateTime repeatTime;
        //TODO: update so time value in minutes is passed with T option
        if (currJobInstance.getRepeat() == Repeat.T) {
            repeatTime = scheduledTime.plus(Duration.ofMinutes(2));
        } else {
            repeatTime = scheduledTime.plus(
                repeatDuration.get(
                    currJobInstance.getRepeat().toString()
                )
            );
        }

        JobInstance repeatJob = new JobInstance();
        repeatJob.setJobName(currJobInstance.getJobName());
        repeatJob.setRepeat(currJobInstance.getRepeat());
        repeatJob.setJobStatus(JobStatus.WAITING);
        repeatJob.setScheduledTime(repeatTime);
        
        jobInstRepo.save(repeatJob);
    }

    public void executeJob(JobInstance jobInstance) {
        jobInstance.setJobStatus(JobStatus.RUNNING);
        jobInstRepo.save(jobInstance);
        executor.runJob(jobInstance);
    }

    public void recheckNextJob() {
        synchronized(lock) {
            // set restart to true so scheduleJobs restarts
            restart = true;
            lock.notify();
        }
    }

}
