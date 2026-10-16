package com.zaid.recruitment.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
//import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Job {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "job")
            @JsonIgnore
    List<Application> application;
    @ManyToOne()
    @JoinColumn(name = "recruiter_id")
    Recruiter recruiter;
}
