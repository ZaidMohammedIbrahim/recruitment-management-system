package com.zaid.recruitment.DTO;

import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Job;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponseDto {
   long id;
    String applicationStatus;
    LocalDateTime appliedAt;
    LocalDateTime updatedAt;
    long jobId;
    String jobName;
    long candidateId;
    String candidateName;
}
