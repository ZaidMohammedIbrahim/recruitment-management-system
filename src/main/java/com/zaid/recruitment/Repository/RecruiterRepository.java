package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Recruiter findByEmail(String email);
}
