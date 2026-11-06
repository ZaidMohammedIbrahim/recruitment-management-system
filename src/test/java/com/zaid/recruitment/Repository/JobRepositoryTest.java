package com.zaid.recruitment.Repository;

import com.zaid.recruitment.Entity.Job;
import com.zaid.recruitment.Entity.Recruiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class JobRepositoryTest {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Test
    void findByLocation() {
        Job job= new Job();
        job.setLocation("Hyderabad");
        job.setTitle("Software Engineering");
        job.setEmploymentType("Full Time");
        Recruiter recruiter= new Recruiter();
        recruiter.setCompanyName("Amazon");
        recruiter.setEmail("recruiter@email.com");
        recruiterRepository.save(recruiter);
        job.setRecruiter(recruiter);
        Job savedJob=jobRepository.save(job);
        Job job1 = new  Job();
        job1.setLocation("Hyderabad");
        job1.setTitle("Software Testing");
        job1.setEmploymentType("Part Time");
        job1.setRecruiter(recruiter);
        Job savedJob1=jobRepository.save(job1);
        List<Job> jobs= jobRepository.findByLocation("Hyderabad");
        assertEquals(2,jobs.size());
        assertEquals(savedJob.getId(),jobs.get(0).getId());
        assertEquals(savedJob.getTitle(),jobs.get(0).getTitle());
        assertEquals(savedJob.getEmploymentType(),jobs.get(0).getEmploymentType());
        assertEquals(savedJob.getRecruiter().getCompanyName(),jobs.get(0).getRecruiter().getCompanyName());
        assertEquals(savedJob.getRecruiter().getEmail(),jobs.get(0).getRecruiter().getEmail());
        assertEquals(savedJob1.getId(),jobs.get(1).getId());
        assertEquals(savedJob1.getTitle(),jobs.get(1).getTitle());
        assertEquals(savedJob1.getEmploymentType(),jobs.get(1).getEmploymentType());
        assertEquals(savedJob1.getRecruiter().getCompanyName(),jobs.get(1).getRecruiter().getCompanyName());
        assertEquals(savedJob1.getRecruiter().getEmail(),jobs.get(1).getRecruiter().getEmail());


    }
   @Test
    void findByTitle() {
        Job job= new Job();
        job.setLocation("Hyderabad");
        job.setTitle("Software Engineering");
        job.setEmploymentType("Full Time");
        Recruiter recruiter= new Recruiter();
        recruiter.setCompanyName("Amazon");
        recruiter.setEmail("recruiter@email.com");
        recruiterRepository.save(recruiter);
        job.setRecruiter(recruiter);
        Job savedJob=jobRepository.save(job);
        Job job1 = new  Job();
        job1.setLocation("Hyderabad");
        job1.setTitle("Software Testing");
        job1.setEmploymentType("Part Time");
        job1.setRecruiter(recruiter);
        Job savedJob1=jobRepository.save(job1);
        List<Job> jobs= jobRepository.findByTitleContainingIgnoreCase("Software Engineering");
        assertEquals(1,jobs.size());
        assertEquals(savedJob.getId(),jobs.get(0).getId());
        assertEquals(savedJob.getTitle(),jobs.get(0).getTitle());
        assertEquals(savedJob.getEmploymentType(),jobs.get(0).getEmploymentType());
        assertEquals(savedJob.getRecruiter().getCompanyName(),jobs.get(0).getRecruiter().getCompanyName());
        assertEquals(savedJob.getRecruiter().getEmail(),jobs.get(0).getRecruiter().getEmail());



    }

}
