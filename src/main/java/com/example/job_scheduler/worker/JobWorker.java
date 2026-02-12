package com.example.job_scheduler.worker;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.job_scheduler.models.Job;
import com.example.job_scheduler.models.JobStatus;
import com.example.job_scheduler.repository.JobRepository;

import jakarta.annotation.PostConstruct;

@Component
public class JobWorker {
    private static final String QUEUE = "job:queue";
    private static final int MAX_RETRIES  = 3;
    private static final int BASE_DELAY_SECONDS = 5;

    private final JobRepository jobRepository;
    private final StringRedisTemplate redisTemplate;

    public JobWorker(JobRepository jobRepository , StringRedisTemplate redisTemplate){
        this.jobRepository = jobRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void start(){
        Thread workerThread = new Thread(this::run);
        workerThread.setDaemon(true);
        workerThread.start();
    }

    private void run(){
        while(true){
            try {
                String jobIdStr = redisTemplate.opsForList().rightPop(QUEUE , 30 , TimeUnit.SECONDS);
                if(jobIdStr==null){
                    continue;
                }
                Long jobId = Long.parseLong(jobIdStr);
                processJob(jobId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processJob(Long jobId){
         Job job = jobRepository.findById(jobId).orElse(null);
         if(job==null){
            return;
         }
         if(job.getStatus()!=JobStatus.PENDING && job.getStatus()!=JobStatus.FAILED){
            return;
         }
         job.setStatus(JobStatus.IN_PROGRESS);
         jobRepository.save(job);
         try {
             executeJob(job);
             job.setStatus(JobStatus.COMPLETED);
             job.setFailureReason(null);
             jobRepository.save(job);
         } catch (Exception e) {
            handleFailure(job , e);
         }
    }

    private void executeJob(Job job) throws Exception{
        System.out.println("Processing job " + job.getId() + " with payload: " + job.getPayload());
        Thread.sleep(2000);

        if(Math.random()<0.5){
            throw new RuntimeException("Simulated job failure");
        }
    }

    private void handleFailure(Job job , Exception e){
        int retries = job.getRetryCount()+1;
        job.setRetryCount(retries);
        job.setFailureReason(e.getMessage());
        if(retries>=MAX_RETRIES){
            job.setStatus(JobStatus.DEAD);
            jobRepository.save(job);
            System.out.println("Job " + job.getId() + " marked as DEAD after " + retries + " retries.");
            return;
        }
        job.setStatus(JobStatus.FAILED);
        jobRepository.save(job);
        int delay = BASE_DELAY_SECONDS * (int)Math.pow(2, retries - 1);
        try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException ignored) {
        }
        redisTemplate.opsForList().rightPush(QUEUE, job.getId().toString());
        System.out.println("Retrying job " + job.getId() + " after failure. Retry count: " + retries);
    }
}

