package com.example.job_scheduler.dto;

public class JobResponseDTO {
    private Long id;
    private String status;
    public JobResponseDTO(Long id, String status){
        this.id = id;
        this.status = status;
    }
    public Long getId(){
        return id;
    }
    public String getStatus(){
        return status;
    }
}