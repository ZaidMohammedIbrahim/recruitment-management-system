package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
List<Application> findByCandidate_id(long Id);
List<Application> findByJob_id(long Id);
boolean existsByCandidate_idAndJob_id(long candidateId, long jobId);
Optional<Application> findByCandidate_Name(String name);
}
