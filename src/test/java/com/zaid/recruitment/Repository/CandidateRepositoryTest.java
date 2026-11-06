package com.zaid.recruitment.Repository;
import com.zaid.recruitment.Auth.UserEntity;
import com.zaid.recruitment.Auth.UserRepository;
import com.zaid.recruitment.Entity.Candidate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
public class CandidateRepositoryTest {
    @Autowired CandidateRepository candidateRepository;
    @Autowired
    UserRepository userRepository;
    @Test
    public void shouldSaveAndFindCandidateByEmail () {
        Candidate candidate = new Candidate();
        candidate.setName("test");
        candidate.setEmail("test@test.com");
        candidate.setPhone("456789000");
        candidateRepository.save(candidate);
        Optional<Candidate> optionalCandidate = candidateRepository.findByEmail("test@test.com");
        assertTrue(optionalCandidate.isPresent());
        assertEquals(candidate.getName(), optionalCandidate.get().getName());
        assertEquals(candidate.getEmail(), optionalCandidate.get().getEmail());
        assertEquals(candidate.getPhone(), optionalCandidate.get().getPhone());
    }
    @Test
    public void shouldGetUserEntityAndCandidateByUserEntityName () {
        UserEntity user = new UserEntity();
        user.setName("test");
        user.setPassword("123");
        user.setRole("ROLE_USER");
        userRepository.save(user);
       Optional<UserEntity> optionalUserEntity = userRepository.findByName("test");
       assertTrue(optionalUserEntity.isPresent());
       assertEquals(user.getName(), optionalUserEntity.get().getName());
        assertEquals(user.getPassword(), optionalUserEntity.get().getPassword());
        assertEquals(user.getRole(), optionalUserEntity.get().getRole());
        Candidate candidate = new Candidate();
        candidate.setUserEntity(user);
        candidate.setExperience("2Years");
        candidate.setPhone("456789000");
        candidateRepository.save(candidate);
        Optional<Candidate> candidate1= candidateRepository.findByUserEntity_Name("test");
        assertTrue(candidate1.isPresent());
        assertEquals(candidate.getExperience(), candidate1.get().getExperience());
        assertEquals(candidate.getPhone(), candidate1.get().getPhone());



    }

    @Test
    public void shouldPullUserWithCandidate() {
        UserEntity user = new UserEntity();
        user.setName("test");
        user.setPassword("123");
        user.setRole("ROLE_USER");
        userRepository.save(user);
        Optional<UserEntity> optionalUser = userRepository.findByName("test");
        assertTrue(optionalUser.isPresent());
        assertEquals(user.getName(), optionalUser.get().getName());
        assertEquals(user.getPassword(), optionalUser.get().getPassword());
        assertEquals(user.getRole(), optionalUser.get().getRole());
        Candidate candidate = new Candidate();
        candidate.setUserEntity(user);
        candidate.setExperience("2Years");
        candidate.setPhone("456789000");
        Candidate savedCandidate = candidateRepository.save(candidate);

        Optional<Candidate> candidate1 =
                candidateRepository.findById(savedCandidate.getId());
        assertTrue(candidate1.isPresent());
        assertEquals(candidate.getExperience(), candidate1.get().getExperience());
        assertEquals(candidate.getPhone(), candidate1.get().getPhone());
        assertEquals("test",candidate1.get().getUserEntity().getName());
        assertEquals("123",candidate1.get().getUserEntity().getPassword());



    }
}
