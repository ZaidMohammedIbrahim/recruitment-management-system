package com.zaid.recruitment.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDtoRecruiter {


        @NotNull
        String username;
        @Email
        String email;
        String phone;
        @Size(min = 8, max = 20)
        @NotNull
        String userPassword;
        String address;
        String experience;
        String companyName;
        //LocalDateTime createdAt;

    }


