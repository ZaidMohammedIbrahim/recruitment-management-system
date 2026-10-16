package com.zaid.recruitment.Controller;

import com.zaid.recruitment.DTO.CandidateRequestDto;
import com.zaid.recruitment.DTO.CandidateResponceDto;
import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Service.CandidateService;
import com.zaid.recruitment.Service.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    CandidateService candidateService;
    CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Message> updateCandidate(@RequestBody CandidateRequestDto candidateRequestDto,@PathVariable("id") long id,Authentication authentication) {

        return new ResponseEntity<>(candidateService.updateCandidate(candidateRequestDto,id,authentication),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'CANDIDATE')")
    public ResponseEntity<Message> deleteCandidate(
            @PathVariable("id") long id,
            Authentication authentication) {
        System.out.println("deleteCandidate");

        candidateService.deleteCandidate(id, authentication);

        return ResponseEntity.ok(new Message("Candidate deleted successfully"));
    }
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateResponceDto> getCandidateById(@PathVariable("id") long id) {
        return new ResponseEntity<>(candidateService.getCandidate(id),HttpStatus.OK);
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<List<CandidateResponceDto>> getAllCandidates() {
        return new ResponseEntity<>(candidateService.getCandidates(),HttpStatus.OK);
    }
    @GetMapping("/getByEmail")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<CandidateResponceDto> getCandidatesByEmail (@RequestParam String email) {
        return new ResponseEntity<>(candidateService.getCandidatesByEmail(email),HttpStatus.OK);
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateResponceDto> getMyProfile(
            Authentication authentication) {

        String username = authentication.getName();

        return new ResponseEntity<>(candidateService.getMyCandidateProfile(username),HttpStatus.OK);
    }
}
