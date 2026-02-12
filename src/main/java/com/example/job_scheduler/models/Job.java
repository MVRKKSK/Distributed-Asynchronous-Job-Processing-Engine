package com.example.job_scheduler.models;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private int retryCount;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    

    //Constructors
    protected Job() {
    }
    // public Job(Long id, JobStatus status, int retryCount, String payload, String result, String failureReason,
    //         Date createdAt, Date updatedAt) {
    //     this.id = id;
    //     this.status = status;
    //     this.retryCount = retryCount;
    //     this.payload = payload;
    //     this.failureReason = failureReason;
    //     this.createdAt = createdAt;
    //     this.updatedAt = updatedAt;
    // }

    // public Job(JobStatus status, int retryCount, String payload, String result, String failureReason,
    //         Date createdAt, Date updatedAt) {
    //     this.status = status;
    //     this.retryCount = retryCount;
    //     this.payload = payload;
    //     this.failureReason = failureReason;
    //     this.createdAt = createdAt;
    //     this.updatedAt = updatedAt;
    // }
    
    public Job(String payload){
        this.payload = payload;
        this.status = JobStatus.PENDING;
        this.retryCount = 0;
    }
    @PrePersist
    void OnCreate(){
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    void OnUpdate(){
        this.updatedAt = new Date();
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public JobStatus getStatus() {
        return status;
    }
    public void setStatus(JobStatus status) {
        this.status = status;
    }
    public int getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public String getFailureReason() {
        return failureReason;
    }
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
