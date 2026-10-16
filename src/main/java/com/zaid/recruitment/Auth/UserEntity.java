package com.zaid.recruitment.Auth;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserEntity {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique = true, nullable = false)
    String name;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String role;
}
