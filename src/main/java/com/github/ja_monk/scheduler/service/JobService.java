package com.github.ja_monk.scheduler.service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ja_monk.scheduler.dto.JobReqDto;
import com.github.ja_monk.scheduler.dto.JobResDto;
import com.github.ja_monk.scheduler.model.Job;
import com.github.ja_monk.scheduler.repository.JobsRepository;

@Service
public class JobService {
    @Autowired
    private JobsRepository jobRepo;

    public JobResDto createJob(JobReqDto jobReqDto) {
        Job job = new Job();
        job.setName(jobReqDto.getName());
        job.setDefinition(jobReqDto.getDefinition());
        
        jobRepo.save(job);

        JobResDto jobResDto = new JobResDto(job);        
        return jobResDto;
    }

    public ArrayList<JobResDto> getAllJobs() {
        Iterable<Job> jobs = jobRepo.findAll();
        ArrayList<JobResDto> jobsResDto = new ArrayList<>();
        
        for (Job job : jobs) {
            JobResDto jobDto = new JobResDto(job);
            jobsResDto.add(jobDto);
        }
        
        return jobsResDto;
    }

    public JobResDto findJob(int id) {
        // find job or throw default exception if no job found
        Job job = jobRepo.findById(id).orElseThrow();
        JobResDto jobResDto = new JobResDto(job);
        
        return jobResDto;
    }

    public JobResDto findJob(String name) {
        // find job or throw default exception if no job found
        Job job = jobRepo.findByName(name).orElseThrow();
        JobResDto jobResDto = new JobResDto(job); 
        return jobResDto;
    }

    public JobResDto updateJob(JobReqDto jobReqDto) {
        // TODO: Update to find by name
        Iterable<Job> jobs = jobRepo.findAll();
        
        for (Job job : jobs) {
            if (job.getName().equals(jobReqDto.getName())) {
                job.setDefinition(jobReqDto.getDefinition());
                jobRepo.save(job);
                JobResDto jobResDto = new JobResDto(job);
                return jobResDto;
            }
        }
        
        throw new NoSuchElementException();
    }

    public void deleteJob(int id) {
        jobRepo.deleteById(id);
    }



}
