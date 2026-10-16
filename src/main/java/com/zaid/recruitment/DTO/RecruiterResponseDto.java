package com.zaid.recruitment.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class RecruiterResponseDto {
    long id;
    String name;
    String email;
    String phone;
    String companyName;
    LocalDateTime createdAt;
}
