package com.zaid.recruitment.Controller;

import com.zaid.recruitment.DTO.ApplicationRequestDto;
import com.zaid.recruitment.DTO.ApplicationResponseDto;
import com.zaid.recruitment.Entity.Application;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Service.ApplicationService;
import com.zaid.recruitment.Service.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<ApplicationResponseDto>> getAllApplications() {
        return new ResponseEntity<>(applicationService.getApplications(), HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER','CANDIDATE')")

    public ResponseEntity<Message> updateApplication(@RequestBody ApplicationRequestDto applicationRequestDto, @PathVariable long id, Authentication authentication) {
        return new ResponseEntity<>(applicationService.updateApplication(applicationRequestDto,id,authentication), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER','CANDIDATE')")

    public ResponseEntity<Message> deleteApplication(@PathVariable("id") Long id,Authentication authentication) {
        return new ResponseEntity<>(applicationService.deleteApplication(id,authentication), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Message> addApplication(@RequestBody ApplicationRequestDto applicationRequestDto) {
        return new ResponseEntity<>(applicationService.addApplication(applicationRequestDto), HttpStatus.CREATED);
    }
    @GetMapping("/getByJobId/{id}")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<List<ApplicationResponseDto>> getApplicationsByJobId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(applicationService.getApplicationsByJob(id), HttpStatus.OK);
    }
    @GetMapping("/getByCandidateId/{id}")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<List<ApplicationResponseDto>> getApplicationsByCandidateId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(applicationService.getApplicationsByCandidateId(id), HttpStatus.OK);
    }
    @GetMapping("/checkIfExists/{candidateId}/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")

    public boolean checkApplicationCidAndJid(@PathVariable("candidateId") long candidateId,@PathVariable("jobId") long jobId) {
        return applicationService.existsByCandidate_idAndJob_id(candidateId, jobId);
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponseDto> getMyApplication(Authentication authentication) {
        return new ResponseEntity<>(applicationService.getMe(authentication),HttpStatus.OK);

    }
}
