package com.zaid.recruitment.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CandidateResponceDto {
    long id;
    String name;
    String email;
    String phone;
    String address;
    String experience;
    LocalDateTime createdAt;
    String resumeUrl;
    List<Long> skillIds;
}
