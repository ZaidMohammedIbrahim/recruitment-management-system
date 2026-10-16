package com.zaid.recruitment.Controller;

import com.zaid.recruitment.DTO.RecruiterRequestDto;
import com.zaid.recruitment.DTO.RecruiterResponseDto;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Service.Message;
import com.zaid.recruitment.Service.RecruiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    RecruiterService recruiterService;
    RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<Message> updateRecruiter(@RequestBody RecruiterRequestDto recruiterRequestDto, @PathVariable("id") long id) {
        return new ResponseEntity<>(recruiterService.updateRecruiter(recruiterRequestDto,id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Message> deleteRecruiter(@PathVariable("id") long recruiterId) {
        return new ResponseEntity<>(recruiterService.deleteRecruiter(recruiterId), HttpStatus.OK);
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<List<RecruiterResponseDto>> getAllRecruiter() {
        return new ResponseEntity<>(recruiterService.getAllRecruiter(),HttpStatus.OK);
    }
    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<RecruiterResponseDto>> findByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(recruiterService.findByEmail(email),HttpStatus.OK);
    }
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('RECRUITER')")

    public ResponseEntity<RecruiterResponseDto> getRecruiterById(@PathVariable("id") long recruiterId) {
        return new ResponseEntity<>(recruiterService.findByRecruiter_id(recruiterId),HttpStatus.OK);
    }



}
