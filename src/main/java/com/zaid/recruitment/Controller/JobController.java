package com.zaid.recruitment.Controller;

import com.zaid.recruitment.DTO.JobRequestDto;
import com.zaid.recruitment.DTO.JobResponseDto;
import com.zaid.recruitment.Entity.Job;
import com.zaid.recruitment.Service.JobService;
import com.zaid.recruitment.Service.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {
    JobService jobService;
    JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/add")
    public ResponseEntity<Message> addJob(@RequestBody JobRequestDto jobRequestDto) {
        return new ResponseEntity<>(jobService.addJob(jobRequestDto), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Message> updateJob(@RequestBody JobRequestDto jobRequestDto,@PathVariable("id") long id) {
        return new ResponseEntity<>(jobService.updateJob(jobRequestDto,id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<Message> deleteJob(@PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.deleteJob(id), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<JobResponseDto> getJobById(@PathVariable("id") long id) {
        return new ResponseEntity<>(jobService.getJob(id), HttpStatus.OK);
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobResponseDto>> getAllJobs() {
        return new ResponseEntity<>(jobService.getJobs(), HttpStatus.OK);
    }
    @GetMapping("/getByLocation")
    public ResponseEntity<List<JobResponseDto>> getAllJobsByLocation(@RequestParam String location) {
        return new ResponseEntity<>(jobService.getJobsByLocation(location), HttpStatus.OK);
    }
    @GetMapping("/getByTitle/{title}")
    public ResponseEntity<List<JobResponseDto>> getAllByJobTitle(@PathVariable("title") String title) {
        return new ResponseEntity<>(jobService.getJobsByJobTitle(title), HttpStatus.OK);
    }
}
