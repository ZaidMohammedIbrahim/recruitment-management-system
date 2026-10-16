package com.zaid.recruitment.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CandidateRequestDto {

    String name;
    String email;
    String phone;
    String password;
    String address;
    String experience;
    //LocalDateTime createdAt;
    String resumeUrl;
    List<Long> skillIds;

}
