package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Application;
import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ApplicationRepositoryTest {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private  JobRepository jobRepository;
    @Test
    public void testFindById() {
        Application application = new Application();
        //application.setId(1L);
        application.setApplicationStatus("pending");
        Application savedApplication=applicationRepository.save(application);
       Optional<Application> application1 = applicationRepository.findById(savedApplication.getId());
       assertTrue(application1.isPresent());
       assertEquals("pending",application1.get().getApplicationStatus());
       assertEquals(savedApplication.getId(),application1.get().getId());
    }

    @Test
    public void shouldReturnSavedCandidateAlongWithApplication() {
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
        Job job = new Job();
        job.setTitle("Software Engineer");
        job.setLocation("Hyderabad");
        Application application = new Application();
        application.setApplicationStatus("pending");
        application.setCandidate(candidate);
        application.setJob(job);
        Application savedApplication= applicationRepository.save(application);
        Optional<Application> application1 = applicationRepository.findById(savedApplication.getId());
        assertTrue(application1.isPresent());
        assertEquals(savedApplication.getId(), application1.get().getId());
        assertEquals("pending",application1.get().getApplicationStatus());
        assertEquals("test",application1.get().getCandidate().getName());
        assertEquals("456789000",application1.get().getCandidate().getPhone());
        assertEquals("test@test.com",application1.get().getCandidate().getEmail());
        assertEquals(job.getTitle(),application1.get().getJob().getTitle());
        assertEquals(job.getLocation(),application1.get().getJob().getLocation());


    }

    @Test
    public  void shouldGetApplicationByJob() {
        Job job = new Job();
        job.setTitle("Software Engineer");
        job.setLocation("Hyderabad");
       Job savedJob= jobRepository.save(job);
        Application application = new Application();
        application.setApplicationStatus("pending");
        application.setJob(job);
        applicationRepository.save(application);
        Application application1 = new Application();
        application1.setApplicationStatus("process");
        application1.setJob(job);
        applicationRepository.save(application1);
        List<Application> application2= applicationRepository.findByJob_id(savedJob.getId());
        assertEquals(2,application2.size());
        assertEquals(application.getId(),application2.get(0).getId());
        assertEquals(application1.getId(),application2.get(1).getId());
        assertEquals("pending",application2.get(0).getApplicationStatus());
        assertEquals("process",application2.get(1).getApplicationStatus());

    }

    @Test
    public  void shouldGetApplicationByCandidateId() {
        Candidate candidate = new Candidate();
        candidate.setName("test");
        candidate.setExperience("2years");
        candidate.setPhone("456789000");
        Candidate savedCandidate = candidateRepository.save(candidate);
        Application application = new Application();
        application.setApplicationStatus("pending");
        application.setCandidate(candidate);
        applicationRepository.save(application);
        Application application1 = new Application();
        application1.setApplicationStatus("process");
        application1.setCandidate(candidate);
        applicationRepository.save(application1);
        List<Application> application2= applicationRepository.findByCandidate_id(savedCandidate.getId());
        assertEquals(2,application2.size());
        assertEquals(application.getId(),application2.get(0).getId());
        assertEquals(application1.getId(),application2.get(1).getId());
        assertEquals("pending",application2.get(0).getApplicationStatus());
        assertEquals("process",application2.get(1).getApplicationStatus());

    }

    @Test
    public  void shouldGetApplicationByCandidateName() {
        Candidate candidate = new Candidate();
        candidate.setName("test");
        candidate.setExperience("2years");
        candidate.setPhone("456789000");
        Candidate savedCandidate = candidateRepository.save(candidate);
        Application application = new Application();
        application.setApplicationStatus("pending");
        application.setCandidate(candidate);
        applicationRepository.save(application);
        Optional<Application> application2= applicationRepository.findByCandidate_Name(savedCandidate.getName());
       assertTrue(application2.isPresent());
        assertEquals(application.getId(),application2.get().getId());
        assertEquals("pending",application2.get().getApplicationStatus());

    }
}
