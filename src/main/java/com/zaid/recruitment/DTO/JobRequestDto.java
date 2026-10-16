package com.zaid.recruitment.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class JobRequestDto {
    String title;
    String description;
    String location;
    int salaryMin;
    int salaryMax;
    int experienceRequired;
    String employmentType;
    String status;
    private Long recruiterId;

}
