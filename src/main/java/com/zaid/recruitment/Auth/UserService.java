package com.zaid.recruitment.Auth;

import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Exception.SkillNotFoundException;
import com.zaid.recruitment.Repository.CandidateRepository;
import com.zaid.recruitment.Repository.RecruiterRepository;
import com.zaid.recruitment.Repository.SkillRepository;
import com.zaid.recruitment.Service.Message;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    CandidateRepository candidateRepository;
    RecruiterRepository recruiterRepository;
    SkillRepository skillRepository;
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CandidateRepository candidateRepository, RecruiterRepository recruiterRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.candidateRepository = candidateRepository;
        this.recruiterRepository = recruiterRepository;
        this.skillRepository = skillRepository;
    }
    @Transactional
    public Message addUserAsCandidate( @RequestBody UserRequestDtoCandidate userRequestDtoCandidate){
        UserEntity userEntity1=new UserEntity();
        userEntity1.setName(userRequestDtoCandidate.getUsername());
        userEntity1.setPassword(passwordEncoder.encode(userRequestDtoCandidate.getUserPassword()));
        userEntity1.setRole("ROLE_CANDIDATE");
        userRepository.save(userEntity1);
       // return new Message("User Added Successfully");
        //Candidate candidate1=new Candidate();
        Candidate candidate = new Candidate();
        //candidate.setName(candidateRequestDto.getName());
        candidate.setEmail(userRequestDtoCandidate.getEmail());
        candidate.setPhone(userRequestDtoCandidate.getPhone());
        candidate.setAddress(userRequestDtoCandidate.getAddress());
        //    candidate.setPassword(candidateRequestDto.getPassword());
        candidate.setExperience(userRequestDtoCandidate.getExperience());
        candidate.setResumeUrl(userRequestDtoCandidate.getResumeUrl());
        candidate.setCreatedAt(LocalDateTime.now());
        candidate.setUserEntity(userEntity1);

        // candidate.setCreatedAt(candidateRequestDto.getCreatedAt());
        List<Long> skills= userRequestDtoCandidate.getSkillIds();
        List<Skills> skillsList = new ArrayList<>();
        for (Long skill : skills) {
            Skills skills1=skillRepository.findById(skill).orElseThrow(()->new SkillNotFoundException("skill does not exists"));
            skillsList.add(skills1);
        }
        candidate.setSkills(skillsList);

        candidateRepository.save(candidate);
        return new Message("Candidate added successfully");
    }
  @Transactional
    public Message addUserAsRecruiter(@RequestBody UserRequestDtoRecruiter userRequestDtoRecruiter){
        UserEntity userEntity1=new UserEntity();
        userEntity1.setName(userRequestDtoRecruiter.getUsername());
        userEntity1.setPassword(passwordEncoder.encode(userRequestDtoRecruiter.getUserPassword()));
        userEntity1.setRole("ROLE_RECRUITER");
        userRepository.save(userEntity1);
        Recruiter recruiter = new Recruiter();
        recruiter.setEmail(userRequestDtoRecruiter.getEmail());
        recruiter.setPhone(userRequestDtoRecruiter.getPhone());
        recruiter.setCompanyName(userRequestDtoRecruiter.getCompanyName());
        recruiter.setCreatedAt(LocalDateTime.now());
        recruiter.setUserEntity(userEntity1);
        recruiterRepository.save(recruiter);
        return   new Message("Recruiter added successfully");
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity=userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder().username(userEntity.getName()).password(userEntity.getPassword()).authorities(new SimpleGrantedAuthority(userEntity.getRole())).build();

    }
}
