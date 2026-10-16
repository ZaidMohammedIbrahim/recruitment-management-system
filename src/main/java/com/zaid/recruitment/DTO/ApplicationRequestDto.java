package com.zaid.recruitment.DTO;

import lombok.Data;

@Data
public class ApplicationRequestDto {
    String applicationStatus;
    long candidateId;
    long jobId;
}
