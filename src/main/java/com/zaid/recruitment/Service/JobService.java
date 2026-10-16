package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.JobRequestDto;
import com.zaid.recruitment.DTO.JobResponseDto;
import com.zaid.recruitment.Entity.Job;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Exception.JobNotFoundException;
import com.zaid.recruitment.Exception.RecruiterNotFoundException;
import com.zaid.recruitment.Repository.JobRepository;
import com.zaid.recruitment.Repository.RecruiterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    JobRepository jobRepository;
    RecruiterRepository recruiterRepository;
    public JobService(JobRepository jobRepository, RecruiterRepository recruiterRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public Message addJob(JobRequestDto jobRequestDto)
    {
        Job job = new Job();
        BeanUtils.copyProperties(jobRequestDto,job);
        Recruiter recruiter = recruiterRepository.findById(jobRequestDto.getRecruiterId()).orElseThrow(()->new RecruiterNotFoundException("RecruiterId not found"));
        job.setRecruiter(recruiter);
        job.setCreatedAt(LocalDateTime.now());
        jobRepository.save(job);
        return new Message("Job has been added successfully");
    }

    public Message updateJob(JobRequestDto dto, long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        BeanUtils.copyProperties(dto, job, "id", "recruiterId", "createdAt");

        if (dto.getRecruiterId() != null) {
            Recruiter recruiter = recruiterRepository.findById(dto.getRecruiterId())
                    .orElseThrow(() ->
                            new RecruiterNotFoundException("RecruiterId not found"));

            job.setRecruiter(recruiter);
        }

        jobRepository.save(job);

        return new Message("Job has been updated successfully");
    }

    public Message deleteJob(long id) {
        jobRepository.deleteById(id);
        return new Message("Job has been deleted successfully");

    }
    public JobResponseDto getJob(long id) {
        Job job= jobRepository.findById(id).orElseThrow(()-> new JobNotFoundException("Job not found"));
        JobResponseDto jobResponseDto = new JobResponseDto();
        BeanUtils.copyProperties(job,jobResponseDto);
        return jobResponseDto;
    }
    public List<JobResponseDto> getJobs()
    {
       List<Job> job= jobRepository.findAll();
       List<JobResponseDto> jobResponseDto=new ArrayList<>();
       for (Job job1:job){
           JobResponseDto jobResponseDto1=new JobResponseDto();
           BeanUtils.copyProperties(job1,jobResponseDto1);
           jobResponseDto.add(jobResponseDto1);
       }
       return jobResponseDto;
    }
    public List<JobResponseDto> getJobsByLocation(String location) {
        List<Job> job= jobRepository.findByLocation(location);
        List<JobResponseDto> jobResponseDto=new ArrayList<>();
        for (Job job1:job){
            JobResponseDto jobResponseDto1=new JobResponseDto();
            BeanUtils.copyProperties(job1,jobResponseDto1);
            jobResponseDto.add(jobResponseDto1);
        }
        return jobResponseDto;
    }

    public List<JobResponseDto> getJobsByJobTitle(String jobTitle) {
        List<Job> job= jobRepository.findByTitleContainingIgnoreCase(jobTitle);
        List<JobResponseDto> jobResponseDto=new ArrayList<>();
        for (Job job1:job){
            JobResponseDto jobResponseDto1=new JobResponseDto();
            BeanUtils.copyProperties(job1,jobResponseDto1);
            jobResponseDto.add(jobResponseDto1);
        }
        return jobResponseDto;
    }
}
