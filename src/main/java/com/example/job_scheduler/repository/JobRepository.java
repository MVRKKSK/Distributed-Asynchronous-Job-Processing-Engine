package com.example.job_scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.job_scheduler.models.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    
}
