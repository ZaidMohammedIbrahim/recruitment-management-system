package com.zaid.recruitment.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zaid.recruitment.Auth.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Candidate {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
   @Email
    String email;
    String name;
    String phone;
    String address;
    String experience;
    LocalDateTime createdAt;
    String resumeUrl;
    @OneToMany(mappedBy = "candidate")
            @JsonIgnore
    List<Application> application;
    @ManyToMany()
    @JoinTable(name = "candidate_skills",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
List<Skills> skills;
    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;
}