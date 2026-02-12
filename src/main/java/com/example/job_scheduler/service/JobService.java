package com.example.job_scheduler.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.models.Job;
import com.example.job_scheduler.repository.JobRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private static final String QUEUE = "job:queue";
    private final StringRedisTemplate redisTemplate;
    public JobService(JobRepository jobRepository , StringRedisTemplate redisTemplate){
        this.jobRepository = jobRepository;
        this.redisTemplate = redisTemplate;
    }
    public Job createJob(String payload){
        Job job = new Job(payload);
        Job savedJob = jobRepository.save(job);

        redisTemplate.opsForList().rightPush(QUEUE, savedJob.getId().toString());
        return savedJob;
    }
    public Job getJob(Long id){
        return jobRepository.findById(id).orElse(null);
    }
}
