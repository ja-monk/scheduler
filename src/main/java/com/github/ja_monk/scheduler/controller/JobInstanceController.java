package com.github.ja_monk.scheduler.controller;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.ja_monk.scheduler.dto.ApiResponse;
import com.github.ja_monk.scheduler.dto.JobInstReqDto;
import com.github.ja_monk.scheduler.dto.JobInstResDto;
import com.github.ja_monk.scheduler.service.JobInstanceService;

@Controller
public class JobInstanceController {

    @Autowired
    private JobInstanceService jobInstService;

    @PostMapping("/proc/submit")
    public ResponseEntity<ApiResponse<JobInstResDto>> submitJob(@RequestBody JobInstReqDto jobInstReqDto) {
        try {
            JobInstResDto jobInstResDto = jobInstService.submitJob(jobInstReqDto);
            return ResponseEntity.ok(ApiResponse.ok(jobInstResDto));
        } catch (Exception e) {
            String errorMessage = "Error creating instance of job: " + jobInstReqDto.getName();
            return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
        }
    }

    @GetMapping("/proc/list")
    public ResponseEntity<ApiResponse<ArrayList<JobInstResDto>>> getAllScheduledJobs() {
        try {
            ArrayList<JobInstResDto> jobInstList = jobInstService.getAllScheduledJobs();
            return ResponseEntity.ok(ApiResponse.ok(jobInstList));
        } catch (Exception e) {
            String errorMessage = "Error finding scheduled jobs";
            return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
        }
    }

    @PostMapping("/proc/cancel/{id}")
    public ResponseEntity<ApiResponse<JobInstResDto>> cancelJob(@PathVariable("id") int id) {
        try {
            JobInstResDto jobInstResDto = jobInstService.cancelJob(id);
            return ResponseEntity.ok(ApiResponse.ok(jobInstResDto));
        } catch (NoSuchElementException e) {
            String errorMessage = "Could not find job with ID " + id;
            return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
        }    
    }

    @PostMapping("/proc/cancel/all")
    public ResponseEntity<ApiResponse<ArrayList<JobInstResDto>>> cancelAllJobs() {
        try {
            ArrayList<JobInstResDto> cancelledJobs = jobInstService.cancelAllJobs();
            return ResponseEntity.ok(ApiResponse.ok(cancelledJobs));
        } catch (Exception e) {
            String errorMeessage = "Error cancelling all jobs";
            return ResponseEntity.badRequest().body(ApiResponse.error(errorMeessage));
        }
    }    
}
