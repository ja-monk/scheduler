package com.github.ja_monk.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartUpService {
    @Autowired
    private SchedulingService scheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void startScheduleService() throws InterruptedException {
        scheduler.scheduleJobs();
    }
}
