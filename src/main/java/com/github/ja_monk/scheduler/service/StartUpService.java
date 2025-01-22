package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class StartUpService {
    @Autowired
    private SchedulingService scheduler;

    @PostConstruct  // PostConstuct tag runs this method on app start
    public void startJobRunnerService() throws InterruptedException {
        scheduler.scheduleJobs();
    }
}
