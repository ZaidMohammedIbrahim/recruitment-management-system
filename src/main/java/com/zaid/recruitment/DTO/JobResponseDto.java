package com.zaid.recruitment.DTO;

import com.zaid.recruitment.Entity.Recruiter;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class JobResponseDto {
    long id;
    String title;
    String description;
    String location;
    int salaryMin;
    int salaryMax;
    int experienceRequired;
    String employmentType;
    String status;
    LocalDateTime createdAt;
    Long recruiterId;
    String recruiterName;
}
