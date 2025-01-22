package com.github.ja_monk.scheduler.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.ja_monk.scheduler.model.JobInstance;
import com.github.ja_monk.scheduler.repository.JobInstanceRepository;


@Service
public class SchedulingService {
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
        // Start while true loop as we want job runner to always be running
        while (true) {
            restart = false;
            // synchronised with method that restarts the loop
            // any time a job is added/updated/etc, we restart the loop and check for next job to run
            synchronized (lock) {
                // find all jobs which have status waiting and have minimum scheduled time
                ArrayList<JobInstance> nextJobsList = jobInstRepo.findNextScheduledJobs();

                // if no jobs scheduled and waiting, then wait for a new job
                if (nextJobsList.isEmpty()) { 
                    lock.wait();
                    continue;   // wait is interupted when notified, we want to redo the check for jobs to run
                }
                
                LocalDateTime scheduledTime = nextJobsList.get(0).getScheduledTime();

                // if time is in the past run all jobs restart loop to check for jobs with next minimum time
                // TODO: run jobs multithreaded 
                if (scheduledTime.isBefore(LocalDateTime.now())) {
                    for (JobInstance jobInstance : nextJobsList) {
                        executor.runJob(jobInstance);
                    }
                    continue;
                }

                // for jobs scheduled in future, find time between now and scheduled time, absolute time to handle edge case
                Long untilScheduledTime = Duration.between(LocalDateTime.now(), scheduledTime).abs().toMillis();

                // wait from now until scheduled time
                lock.wait(untilScheduledTime);

                // if we have been notified this means we want to redo check for jobs 
                // this variable should then be true
                if (restart == true) {
                    continue;
                }
                
                // if we came out of wait naturally, then time should now be scheduled time so we run jobs
                for (JobInstance jobInstance : nextJobsList) {
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
