
package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.JobRequestDto;
import com.zaid.recruitment.DTO.JobResponseDto;
import com.zaid.recruitment.Entity.Job;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Exception.JobNotFoundException;
import com.zaid.recruitment.Exception.RecruiterNotFoundException;
import com.zaid.recruitment.Repository.JobRepository;
import com.zaid.recruitment.Repository.RecruiterRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @InjectMocks
    private JobService jobService;


    // 1. ADD JOB - SUCCESS

    @Test
    void shouldAddJob() {

        JobRequestDto dto = new JobRequestDto();
        dto.setTitle("Java Developer");
        dto.setLocation("Hyderabad");
        dto.setRecruiterId(1L);

        Recruiter recruiter = new Recruiter();
        recruiter.setId(1L);

        when(recruiterRepository.findById(1L))
                .thenReturn(Optional.of(recruiter));

        Message result = jobService.addJob(dto);

        assertEquals("Job has been added successfully",
                result.getMessage());

        verify(recruiterRepository).findById(1L);

        verify(jobRepository).save(argThat(job ->
                "Java Developer".equals(job.getTitle())
                        && "Hyderabad".equals(job.getLocation())
                        && job.getRecruiter() == recruiter
                        && job.getCreatedAt() != null
        ));
    }


    // 2. ADD JOB - RECRUITER NOT FOUND

    @Test
    void shouldThrowExceptionWhenAddingJobWithInvalidRecruiter() {

        JobRequestDto dto = new JobRequestDto();
        dto.setTitle("Java Developer");
        dto.setRecruiterId(99L);

        when(recruiterRepository.findById(99L))
                .thenReturn(Optional.empty());

        RecruiterNotFoundException exception = assertThrows(
                RecruiterNotFoundException.class,
                () -> jobService.addJob(dto)
        );

        assertEquals("RecruiterId not found", exception.getMessage());

        verify(jobRepository, never()).save(any(Job.class));
    }


    // 3. UPDATE JOB - SUCCESS WITHOUT CHANGING RECRUITER

    @Test
    void shouldUpdateJob() {

        Job existingJob = new Job();
        existingJob.setId(1L);
        existingJob.setTitle("Java Developer");
        existingJob.setLocation("Hyderabad");

        JobRequestDto dto = new JobRequestDto();
        dto.setTitle("Senior Java Developer");
        dto.setLocation("Bengaluru");
        // recruiterId left null

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(existingJob));

        Message result = jobService.updateJob(dto, 1L);

        assertEquals("Job has been updated successfully",
                result.getMessage());

        assertEquals("Senior Java Developer", existingJob.getTitle());
        assertEquals("Bengaluru", existingJob.getLocation());
        assertEquals(1L, existingJob.getId());

        verify(jobRepository).save(existingJob);
        verifyNoInteractions(recruiterRepository);
    }


    // 4. UPDATE JOB - JOB NOT FOUND

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingJob() {

        JobRequestDto dto = new JobRequestDto();
        dto.setTitle("Senior Java Developer");

        when(jobRepository.findById(99L))
                .thenReturn(Optional.empty());

        JobNotFoundException exception = assertThrows(
                JobNotFoundException.class,
                () -> jobService.updateJob(dto, 99L)
        );

        assertEquals("Job not found", exception.getMessage());

        verify(jobRepository, never()).save(any(Job.class));
        verifyNoInteractions(recruiterRepository);
    }


    // 5. UPDATE JOB - RECRUITER NOT FOUND

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidRecruiter() {

        Job existingJob = new Job();
        existingJob.setId(1L);
        existingJob.setTitle("Java Developer");

        JobRequestDto dto = new JobRequestDto();
        dto.setTitle("Senior Java Developer");
        dto.setRecruiterId(99L);

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(existingJob));

        when(recruiterRepository.findById(99L))
                .thenReturn(Optional.empty());

        RecruiterNotFoundException exception = assertThrows(
                RecruiterNotFoundException.class,
                () -> jobService.updateJob(dto, 1L)
        );

        assertEquals("RecruiterId not found", exception.getMessage());

        verify(jobRepository, never()).save(any(Job.class));
    }


    // 6. UPDATE JOB - CHANGE RECRUITER

    @Test
    void shouldUpdateJobRecruiter() {

        Job existingJob = new Job();
        existingJob.setId(1L);
        existingJob.setTitle("Java Developer");

        Recruiter newRecruiter = new Recruiter();
        newRecruiter.setId(2L);

        JobRequestDto dto = new JobRequestDto();
        dto.setTitle("Senior Java Developer");
        dto.setRecruiterId(2L);

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(existingJob));

        when(recruiterRepository.findById(2L))
                .thenReturn(Optional.of(newRecruiter));

        Message result = jobService.updateJob(dto, 1L);

        assertEquals("Job has been updated successfully",
                result.getMessage());

        assertEquals("Senior Java Developer", existingJob.getTitle());
        assertSame(newRecruiter, existingJob.getRecruiter());

        verify(recruiterRepository).findById(2L);
        verify(jobRepository).save(existingJob);
    }


    // 7. DELETE JOB - SUCCESS

    @Test
    void shouldDeleteJob() {

        Job job = new Job();
        job.setId(1L);

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(job));

        Message result = jobService.deleteJob(1L);

        assertEquals("Job has been deleted successfully",
                result.getMessage());

        verify(jobRepository).findById(1L);
        verify(jobRepository).deleteById(1L);
    }


    // 8. DELETE JOB - NOT FOUND

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingJob() {

        when(jobRepository.findById(99L))
                .thenReturn(Optional.empty());

        JobNotFoundException exception = assertThrows(
                JobNotFoundException.class,
                () -> jobService.deleteJob(99L)
        );

        assertEquals("Job not found", exception.getMessage());

        verify(jobRepository, never()).deleteById(anyLong());
    }


    // 9. GET JOB BY ID - SUCCESS

    @Test
    void shouldGetJobById() {

        Job job = new Job();
        job.setId(1L);
        job.setTitle("Java Developer");
        job.setLocation("Hyderabad");

        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(job));

        JobResponseDto result = jobService.getJob(1L);

        assertEquals(1L, result.getId());
        assertEquals("Java Developer", result.getTitle());
        assertEquals("Hyderabad", result.getLocation());

        verify(jobRepository).findById(1L);
    }


    // 10. GET JOB BY ID - NOT FOUND

    @Test
    void shouldThrowExceptionWhenGettingNonExistingJob() {

        when(jobRepository.findById(99L))
                .thenReturn(Optional.empty());

        JobNotFoundException exception = assertThrows(
                JobNotFoundException.class,
                () -> jobService.getJob(99L)
        );

        assertEquals("Job not found", exception.getMessage());
    }


    // 11. GET ALL JOBS - SUCCESS

    @Test
    void shouldGetAllJobs() {

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Java Developer");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("Backend Developer");

        when(jobRepository.findAll())
                .thenReturn(List.of(job1, job2));

        List<JobResponseDto> result = jobService.getJobs();

        assertEquals(2, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());
        assertEquals("Backend Developer", result.get(1).getTitle());

        verify(jobRepository).findAll();
    }


    // 12. GET ALL JOBS - EMPTY LIST

    @Test
    void shouldReturnEmptyListWhenNoJobsExist() {

        when(jobRepository.findAll())
                .thenReturn(List.of());

        List<JobResponseDto> result = jobService.getJobs();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(jobRepository).findAll();
    }


    // 13. GET JOBS BY LOCATION - SUCCESS

    @Test
    void shouldGetJobsByLocation() {

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Java Developer");
        job1.setLocation("Hyderabad");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("Backend Developer");
        job2.setLocation("Hyderabad");

        when(jobRepository.findByLocation("Hyderabad"))
                .thenReturn(List.of(job1, job2));

        List<JobResponseDto> result =
                jobService.getJobsByLocation("Hyderabad");

        assertEquals(2, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());
        assertEquals("Hyderabad", result.get(0).getLocation());

        verify(jobRepository).findByLocation("Hyderabad");
    }


    // 14. GET JOBS BY LOCATION - NO MATCHES

    @Test
    void shouldReturnEmptyListWhenLocationHasNoJobs() {

        when(jobRepository.findByLocation("Delhi"))
                .thenReturn(List.of());

        List<JobResponseDto> result =
                jobService.getJobsByLocation("Delhi");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(jobRepository).findByLocation("Delhi");
    }


    // 15. GET JOBS BY TITLE - SUCCESS

    @Test
    void shouldGetJobsByTitle() {

        Job job1 = new Job();
        job1.setId(1L);
        job1.setTitle("Java Developer");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setTitle("Senior Java Developer");

        when(jobRepository.findByTitleContainingIgnoreCase("Java"))
                .thenReturn(List.of(job1, job2));

        List<JobResponseDto> result =
                jobService.getJobsByJobTitle("Java");

        assertEquals(2, result.size());
        assertEquals("Java Developer", result.get(0).getTitle());
        assertEquals("Senior Java Developer", result.get(1).getTitle());

        verify(jobRepository)
                .findByTitleContainingIgnoreCase("Java");
    }


    // 16. GET JOBS BY TITLE - NO MATCHES

    @Test
    void shouldReturnEmptyListWhenTitleHasNoMatches() {

        when(jobRepository.findByTitleContainingIgnoreCase("Pilot"))
                .thenReturn(List.of());

        List<JobResponseDto> result =
                jobService.getJobsByJobTitle("Pilot");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(jobRepository)
                .findByTitleContainingIgnoreCase("Pilot");
    }
}
