package com.github.ja_monk.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.ja_monk.scheduler.dto.JobInstReqDto;
import com.github.ja_monk.scheduler.dto.JobInstResDto;
import com.github.ja_monk.scheduler.service.JobInstanceService;

@Controller
public class JobInstanceController {

    @Autowired
    private JobInstanceService jobInstService;

    @PostMapping("/proc/submit")
    public ResponseEntity<JobInstResDto> submitJob(@RequestBody JobInstReqDto jobInstReqDto) {
        JobInstResDto jobInstResDto = jobInstService.submitJob(jobInstReqDto);

        return ResponseEntity.ok(jobInstResDto);
    }

    @PostMapping("/proc/cancel/{id}")
    public ResponseEntity<JobInstResDto> cancelJob(@PathVariable("id") int id) {
        JobInstResDto jobInstResDto = jobInstService.cancelJob(id);

        return ResponseEntity.ok(jobInstResDto);
    }
}
