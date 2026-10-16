package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String email);
   // Optional<Candidate> findByName(String name);
    Optional<Candidate> findByUserEntity_Name(String name);
}
