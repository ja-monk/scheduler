package com.github.ja_monk.scheduler.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.ja_monk.scheduler.dto.JobReqDto;
import com.github.ja_monk.scheduler.dto.JobResDto;
import com.github.ja_monk.scheduler.service.JobService;

@RestController     // tells spring to automatically handle http requests
public class JobController {
   
    @Autowired
    private JobService jobService;

    @PostMapping("/createjob")
    public ResponseEntity<JobResDto> createJob(@RequestBody JobReqDto jobReqDto) { // @RequestBody tells spring to map JSON to object
        JobResDto createdJob = jobService.createJob(jobReqDto);

        return ResponseEntity.ok(createdJob);
    }

    @GetMapping("/jobs")
    public ArrayList<JobResDto> getAllJobs() {
        ArrayList<JobResDto> allJobs = jobService.getAllJobs();

        return allJobs;
    }

    @GetMapping("/job/id/{id}")
    public JobResDto getJobInfo(@PathVariable("id") int id) {
        JobResDto jobResDto = jobService.findJob(id);
        
        return jobResDto;
    }

    @GetMapping("/job/name/{name}")
    public JobResDto getJobInfo(@PathVariable("name") String name) {
        JobResDto jobResDto = jobService.findJob(name);

        return jobResDto;
    }

    @PutMapping("updatejob")
    public ResponseEntity<JobResDto> updateJob(@RequestBody JobReqDto jobReqDto) {
        JobResDto updatedJob = jobService.updateJob(jobReqDto);
        
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/deletejob/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") int id) {
        jobService.deleteJob(id);
        
        return ResponseEntity.noContent().build();
    }

}
