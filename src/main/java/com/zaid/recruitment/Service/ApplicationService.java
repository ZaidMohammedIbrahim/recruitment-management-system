package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.ApplicationRequestDto;
import com.zaid.recruitment.DTO.ApplicationResponseDto;
import com.zaid.recruitment.Entity.Application;
import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Job;
import com.zaid.recruitment.Exception.ApplicationNotFoundException;
import com.zaid.recruitment.Exception.CandidateNotFoundException;
import com.zaid.recruitment.Exception.JobNotFoundException;
import com.zaid.recruitment.Repository.ApplicationRepository;
import com.zaid.recruitment.Repository.CandidateRepository;
import com.zaid.recruitment.Repository.JobRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    ApplicationRepository applicationRepository;
    CandidateRepository candidateRepository;
    JobRepository jobRepository;
    ApplicationService(ApplicationRepository applicationRepository, CandidateRepository candidateRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
    }
    public ApplicationResponseDto getApplication(long id) {
        Application application= applicationRepository.findById(id).orElseThrow(()->new ApplicationNotFoundException("Application not found"));
        ApplicationResponseDto applicationResponseDto = getApplicationResponseDtoFromApplication(application);
        applicationResponseDto.setCandidateId(application.getCandidate().getId());
       // applicationResponseDto.setCandidateName(application.getCandidate().getName());
        applicationResponseDto.setJobId(application.getJob().getId());
        applicationResponseDto.setJobName(application.getJob().getTitle());
       // BeanUtils.copyProperties(application,applicationResponseDto);

        return applicationResponseDto;
    }
    // method used for converting to Response DTO
    public ApplicationResponseDto getApplicationResponseDtoFromApplication(Application application) {
        ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto();
        BeanUtils.copyProperties(application,applicationResponseDto);
        return applicationResponseDto;
    }
    public List<ApplicationResponseDto> getApplications() {
        List<Application> application=applicationRepository.findAll();
        List<ApplicationResponseDto> applicationResponseDto=new ArrayList<>();
        for(Application application1:application){
            ApplicationResponseDto applicationResponseDto1= getApplicationResponseDtoFromApplication(application1);
            applicationResponseDto1.setCandidateId(application1.getCandidate().getId());
           // applicationResponseDto1.setCandidateName(application1.getCandidate().getName());
            applicationResponseDto1.setJobId(application1.getJob().getId());
            applicationResponseDto1.setJobName(application1.getJob().getTitle());
            applicationResponseDto.add(applicationResponseDto1);
        }
        return applicationResponseDto;
    }
    public Message updateApplication(ApplicationRequestDto applicationRequestDto, long id, Authentication authentication) {
      Application application= applicationRepository.findById(id).orElseThrow(()->new ApplicationNotFoundException("Application not found"));
        Candidate candidate= candidateRepository.findById(applicationRequestDto.getCandidateId()).orElseThrow(()->new CandidateNotFoundException("Candidate not found"));
        String authName=authentication.getName();
        boolean checkName= candidate.getUserEntity().getName().equals(authName);
        if(!checkName){
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to perform this action{Id did not match}");

        }
        Job job= jobRepository.findById(applicationRequestDto.getJobId()).orElseThrow(()->new JobNotFoundException("Job not found"));
      BeanUtils.copyProperties(applicationRequestDto,application);
      application.setJob(job);
      application.setCandidate(candidate);
      application.setUpdatedAt(LocalDateTime.now());
        applicationRepository.save(application);
        return new Message("Application updated successfully");
    }
    public Message deleteApplication(long id,Authentication authentication) {
        Application application=applicationRepository.findById(id).orElseThrow(()->new ApplicationNotFoundException("Application not found"));
        String candidateName=application.getCandidate().getUserEntity().getName();
        boolean checkName= candidateName.equals(authentication.getName());
        boolean isRecruiter = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));
        if(!checkName && !isRecruiter){
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to perform this action");
        }
        applicationRepository.deleteById(id);
        return new Message("Application deleted successfully");
    }
    public Message addApplication(ApplicationRequestDto applicationRequestDto) {
        Application application= new Application();
        BeanUtils.copyProperties(applicationRequestDto,application);
        Candidate candidate= candidateRepository.findById(applicationRequestDto.getCandidateId()).orElseThrow(()->new CandidateNotFoundException("Candidate not found"));
        Job job= jobRepository.findById(applicationRequestDto.getJobId()).orElseThrow(()->new JobNotFoundException("Job not found"));
        application.setCandidate(candidate);
        application.setJob(job);
        application.setAppliedAt(LocalDateTime.now());
        applicationRepository.save(application);
        return new Message("Application added successfully");
    }
    public List<ApplicationResponseDto> getApplicationsByJob(long id) {
        List<Application> applications= applicationRepository.findByJob_id(id);
        List<ApplicationResponseDto> applicationResponseDto=new ArrayList<>();
        for(Application application1:applications){
            ApplicationResponseDto applicationResponseDto1= getApplicationResponseDtoFromApplication(application1);
            applicationResponseDto1.setCandidateId(application1.getCandidate().getId());
           // applicationResponseDto1.setCandidateName(application1.getCandidate().getName());
            applicationResponseDto1.setJobId(application1.getJob().getId());
            applicationResponseDto1.setJobName(application1.getJob().getTitle());
            applicationResponseDto.add(applicationResponseDto1);
        }
        return applicationResponseDto;
    }
    public List<ApplicationResponseDto> getApplicationsByCandidateId(long candidateId) {
        List<Application> applications= applicationRepository.findByCandidate_id(candidateId);
        List<ApplicationResponseDto> applicationResponseDto=new ArrayList<>();
        for(Application application1:applications){
            ApplicationResponseDto applicationResponseDto1= getApplicationResponseDtoFromApplication(application1);
            applicationResponseDto1.setCandidateId(application1.getCandidate().getId());
           // applicationResponseDto1.setCandidateName(application1.getCandidate().getName());
            applicationResponseDto1.setJobId(application1.getJob().getId());
            applicationResponseDto1.setJobName(application1.getJob().getTitle());
            applicationResponseDto.add(applicationResponseDto1);
        }
        return applicationResponseDto;
    }
    public boolean existsByCandidate_idAndJob_id(long candidateId, long jobId) {
        return applicationRepository.existsByCandidate_idAndJob_id(candidateId,jobId);
    }
    public ApplicationResponseDto getMe(Authentication authentication) {
        String authName=authentication.getName();
       Application application= applicationRepository.findByCandidate_Name(authName).orElseThrow(()->new CandidateNotFoundException("Candidate not found"));
       ApplicationResponseDto applicationResponseDto= getApplicationResponseDtoFromApplication(application);
       return applicationResponseDto;
    }
}
