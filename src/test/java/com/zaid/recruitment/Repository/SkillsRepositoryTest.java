package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Skills;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SkillsRepositoryTest {
    @Autowired
    SkillRepository skillRepository;


    @Test
    public void findAll(){
        Skills skills=new Skills();
        skills.setName("Software Engineering");
        skills.setDescription("Experienced Engineer");
        Skills savedSkill1=skillRepository.save(skills);
        Skills skills2=new Skills();
        skills2.setName("Software Testing");
        skills2.setDescription(" Fresher Engineer but with practical knowledge  ");
        Skills savedSkill2=skillRepository.save(skills2);
        List<Skills> skillslist=skillRepository.findAll();
        assertEquals(2,skillslist.size());
        assertEquals(savedSkill1.getId(),skillslist.get(0).getId());
        assertEquals(savedSkill2.getId(),skillslist.get(1).getId());
        assertEquals(savedSkill1.getName(),skillslist.get(0).getName());
        assertEquals(savedSkill2.getName(),skillslist.get(1).getName());
        assertEquals(savedSkill1.getDescription(),skillslist.get(0).getDescription());
        assertEquals(savedSkill2.getDescription(),skillslist.get(1).getDescription());
    }

    @Test
    public void findByName(){
        Skills skills=new Skills();
        skills.setName("Software Engineering");
        skills.setDescription("Experienced Engineer");
        Skills savedSkill1=skillRepository.save(skills);
        Skills skills2=new Skills();
        skills2.setName("Software Testing");
        skills2.setDescription(" Fresher Engineer but with practical knowledge  ");
        Skills savedSkill2=skillRepository.save(skills2);
        List<Skills> skillslist=skillRepository.findSkillsByName("Software Testing");
        assertEquals(1,skillslist.size());
        assertEquals(savedSkill2.getId(),skillslist.get(0).getId());
        assertEquals(savedSkill2.getName(),skillslist.get(0).getName());
        assertEquals(savedSkill2.getDescription(),skillslist.get(0).getDescription());
    }
}
