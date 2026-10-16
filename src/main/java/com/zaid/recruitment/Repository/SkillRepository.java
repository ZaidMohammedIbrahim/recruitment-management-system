package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skills,Long> {
    List<Skills> findSkillsByName(String name);
}
