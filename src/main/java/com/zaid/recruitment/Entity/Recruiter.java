package com.zaid.recruitment.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zaid.recruitment.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    //String name;
    String email;
    String phone;
    String companyName;
    LocalDateTime createdAt;
    @OneToMany(mappedBy = "recruiter")
    @JsonIgnore
    List<Job> job;
@OneToOne
@JoinColumn(name = "user_id")
UserEntity userEntity;
}
