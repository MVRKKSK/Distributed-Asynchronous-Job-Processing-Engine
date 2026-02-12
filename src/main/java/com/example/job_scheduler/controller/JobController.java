package com.example.job_scheduler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_scheduler.dto.JobRequestDTO;
import com.example.job_scheduler.dto.JobResponseDTO;
import com.example.job_scheduler.models.Job;
import com.example.job_scheduler.service.JobService;


@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService){
        this.jobService = jobService;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public JobResponseDTO createJob(@RequestBody JobRequestDTO jobRequestDTO){
        Job job = jobService.createJob(jobRequestDTO.getPayload());
        return new JobResponseDTO(job.getId(), job.getStatus().name());
    }
}
