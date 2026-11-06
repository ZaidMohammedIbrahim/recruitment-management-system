package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.ApplicationRequestDto;
import com.zaid.recruitment.DTO.ApplicationResponseDto;
import com.zaid.recruitment.Entity.Application;
import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Job;
import com.zaid.recruitment.Exception.ApplicationNotFoundException;
import com.zaid.recruitment.Repository.ApplicationRepository;
import com.zaid.recruitment.Repository.CandidateRepository;
import com.zaid.recruitment.Repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


//import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
//import static sun.security.krb5.internal.crypto.Nonce.value;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    CandidateRepository candidateRepository;
    @Mock
    JobRepository jobRepository;
    @InjectMocks
    ApplicationService applicationService;
    @Test
    void shouldGetApplicationResponseDtoFromApplication(){
        Application application=new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Job job=new Job();
        job.setId(1L);
        application.setJob(job);
        Candidate candidate=new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        ApplicationResponseDto applicationResponseDto=applicationService.getApplicationResponseDtoFromApplication(application);
        assertEquals("Zaid",applicationResponseDto.getCandidateName());
        assertEquals("ACTIVE",applicationResponseDto.getApplicationStatus());
        assertEquals(1L,applicationResponseDto.getCandidateId());
        assertEquals(1L,applicationResponseDto.getJobId());


    }
    @Test
    void shouldAddApplication(){
        ApplicationRequestDto  applicationRequestDto=new ApplicationRequestDto();
        applicationRequestDto.setApplicationStatus("ACTIVE");
        Job job=new Job();
        job.setId(1L);
        applicationRequestDto.setJobId(1L);
        Candidate candidate=new Candidate();
        candidate.setId(2L);
        candidate.setName("Zaid");
        applicationRequestDto.setCandidateId(2L);
        when(candidateRepository.findById(2L)).thenReturn(Optional.of(candidate));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        Message message=applicationService.addApplication(applicationRequestDto);
        assertEquals("Application added successfully",message.getMessage());
        verify(applicationRepository,times(1)).save(argThat(application->application.getApplicationStatus().equals("ACTIVE")&&application.getJob().getId()==1L&&application.getCandidate().getName().equals("Zaid")));

    }
    @Test
    void shouldGetApplicationById(){
        Application application=new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Job job=new Job();
        job.setId(1L);
        application.setJob(job);
        Candidate candidate=new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        ApplicationResponseDto applicationResponseDto=applicationService.getApplication(1L);
        assertEquals("Zaid",applicationResponseDto.getCandidateName());
        assertEquals("ACTIVE",applicationResponseDto.getApplicationStatus());
        assertEquals(1L,applicationResponseDto.getCandidateId());
        assertEquals(1L,applicationResponseDto.getJobId());
        verify(applicationRepository,times(1)).findById(1L);
    }
    @Test
    void shouldGetApplications(){
        Application application=new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Job job=new Job();
        job.setId(1L);
        application.setJob(job);
        Candidate candidate=new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        Application application1=new Application();
        application1.setId(1L);
        application1.setApplicationStatus("ACTIVE");
        Job job1=new Job();
        job1.setId(2L);
        application1.setJob(job1);
        Candidate candidate1=new Candidate();
        candidate1.setId(2L);
        candidate1.setName("Mohammed");
        application1.setCandidate(candidate1);
        when(applicationRepository.findAll()).thenReturn(Arrays.asList(application,application1));
        List<ApplicationResponseDto> applicationResponseDtos=applicationService.getApplications();
        assertEquals(2,applicationResponseDtos.size());
        assertEquals(1L,applicationResponseDtos.get(0).getId());
        assertEquals(1L,applicationResponseDtos.get(1).getId());
        assertEquals("ACTIVE",applicationResponseDtos.get(0).getApplicationStatus());
        assertEquals("ACTIVE",applicationResponseDtos.get(1).getApplicationStatus());
        assertEquals(1L,applicationResponseDtos.get(0).getJobId());
        assertEquals(2L,applicationResponseDtos.get(1).getJobId());
        assertEquals(1L,applicationResponseDtos.get(0).getCandidateId());
        assertEquals(2L,applicationResponseDtos.get(1).getCandidateId());
        assertEquals("Zaid",applicationResponseDtos.get(0).getCandidateName());
        assertEquals("Mohammed",applicationResponseDtos.get(1).getCandidateName());
        verify(applicationRepository,times(1)).findAll();

    }
    @Test
    void shouldGetApplicationsByJobId() {
        Application application = new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Job job = new Job();
        job.setId(1L);
        application.setJob(job);
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        Application application1 = new Application();
        application1.setId(2L);
        application1.setApplicationStatus("ACTIVE");
        application1.setJob(job);
        Candidate candidate1 = new Candidate();
        candidate1.setId(2L);
        candidate1.setName("Mohammed");
        application1.setCandidate(candidate1);
        when(applicationRepository.findByJob_id(1L)).thenReturn(Arrays.asList(application,application1));
        List<ApplicationResponseDto> applicationResponseDtos=applicationService.getApplicationsByJob(1L);
        assertEquals(2,applicationResponseDtos.size());
        assertEquals(1L,applicationResponseDtos.get(0).getId());
        assertEquals(2L,applicationResponseDtos.get(1).getId());
        assertEquals("ACTIVE",applicationResponseDtos.get(0).getApplicationStatus());
        assertEquals("ACTIVE",applicationResponseDtos.get(1).getApplicationStatus());
        assertEquals(1L,applicationResponseDtos.get(0).getJobId());
        assertEquals(1L,applicationResponseDtos.get(1).getJobId());
        assertEquals(1L,applicationResponseDtos.get(0).getCandidateId());
        assertEquals(2L,applicationResponseDtos.get(1).getCandidateId());
        assertEquals("Zaid",applicationResponseDtos.get(0).getCandidateName());
        assertEquals("Mohammed",applicationResponseDtos.get(1).getCandidateName());
        verify(applicationRepository,times(1)).findByJob_id(1L);
    }
    @Test
    void shouldGetApplicationOfUser(){
        Application application = new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Job job = new Job();
        job.setId(1L);
        application.setJob(job);
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        Authentication authentication=mock(Authentication.class);
        when(authentication.getName()).thenReturn("Zaid");
        when(applicationRepository.findByCandidate_Name(authentication.getName())).thenReturn(Optional.of(application));
        ApplicationResponseDto applicationResponseDto=applicationService.getMe(authentication);
        assertEquals(1L,applicationResponseDto.getId());
        assertEquals("ACTIVE",applicationResponseDto.getApplicationStatus());
       assertEquals("Zaid",applicationResponseDto.getCandidateName());
       assertEquals(1L,applicationResponseDto.getJobId());
       assertEquals(1L,applicationResponseDto.getCandidateId());
       verify(applicationRepository,times(1)).findByCandidate_Name(authentication.getName());
    }
     @Test
    void shouldGetApplicationsByCandidateId() {
        Application application = new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Job job = new Job();
        job.setId(1L);
        application.setJob(job);
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        Application application1 = new Application();
        application1.setId(2L);
        application1.setApplicationStatus("ACTIVE");
        Job job1 = new Job();
        job1.setId(2L);
        application1.setJob(job1);
        application1.setCandidate(candidate);
        when(applicationRepository.findByCandidate_id(1L)).thenReturn(Arrays.asList(application,application1));
        List<ApplicationResponseDto> applicationResponseDtos=applicationService.getApplicationsByCandidateId(1L);
        assertEquals(2,applicationResponseDtos.size());
        assertEquals(1L,applicationResponseDtos.get(0).getId());
        assertEquals(2L,applicationResponseDtos.get(1).getId());
        assertEquals("ACTIVE",applicationResponseDtos.get(0).getApplicationStatus());
        assertEquals("ACTIVE",applicationResponseDtos.get(1).getApplicationStatus());
        assertEquals(1L,applicationResponseDtos.get(0).getJobId());
        assertEquals(2L,applicationResponseDtos.get(1).getJobId());
        assertEquals(1L,applicationResponseDtos.get(0).getCandidateId());
        assertEquals(1L,applicationResponseDtos.get(1).getCandidateId());
        assertEquals("Zaid",applicationResponseDtos.get(0).getCandidateName());
        assertEquals("Zaid",applicationResponseDtos.get(1).getCandidateName());
        verify(applicationRepository,times(1)).findByCandidate_id(1L);
    }

    @Test
    void shouldDeleteApplicationAfterCheckCandidates(){
        Application application = new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        Authentication authentication=mock(Authentication.class);
        when(authentication.getName()).thenReturn("Zaid");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_CANDIDATE"))).when(authentication).getAuthorities();
        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        Message message=applicationService.deleteApplication(application.getId(),authentication);
        assertEquals("Application deleted successfully",message.getMessage());
        verify(applicationRepository,times(1)).findById(application.getId());
        verify(applicationRepository,times(1)).deleteById(application.getId());



    }

    @Test
    void shouldDeleteApplicationAfterCheckRecruiters(){
        Application application = new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        application.setCandidate(candidate);
        Authentication authentication=mock(Authentication.class);
        when(authentication.getName()).thenReturn("Zaid");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_RECRUITER"))).when(authentication).getAuthorities();
        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        Message message=applicationService.deleteApplication(application.getId(),authentication);
        assertEquals("Application deleted successfully",message.getMessage());
        verify(applicationRepository,times(1)).findById(application.getId());
        verify(applicationRepository,times(1)).deleteById(application.getId());
    }
    @Test
    void shouldThrowExceptionOnDeleteCheck(){
        Application application = new Application();
        application.setId(1L);
        application.setApplicationStatus("ACTIVE");
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Mohammed");
        application.setCandidate(candidate);
        Authentication authentication=mock(Authentication.class);
        when(authentication.getName()).thenReturn("Zaid");
        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        assertThrows(AccessDeniedException.class,()->applicationService.deleteApplication(application.getId(),authentication));
        verify(applicationRepository,never()).deleteById(application.getId());
    }

    @Test
    void shouldThrowExceptionOnDeleteApplicationIfNotExists(){
        Authentication authentication=mock(Authentication.class);
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());
        ApplicationNotFoundException exception= assertThrows(ApplicationNotFoundException.class,()->applicationService.deleteApplication(1L,authentication));
        assertEquals("Application not found",exception.getMessage());
        verify(applicationRepository,never()).deleteById(1L);

    }
}
