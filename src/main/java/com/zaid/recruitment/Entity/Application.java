package com.zaid.recruitment.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Application {
    @Id
    @GeneratedValue
    private long id;
    String applicationStatus;
    LocalDateTime appliedAt;
    LocalDateTime updatedAt;
    @ManyToOne()
    @JoinColumn(name = "candidate_id")
    Candidate candidate;
    @ManyToOne()
    @JoinColumn(name = "job_id")
    Job job;
}
