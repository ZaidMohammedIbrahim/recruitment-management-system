package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.CandidateRequestDto;
import com.zaid.recruitment.DTO.CandidateResponceDto;
import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Exception.CandidateNotFoundException;
import com.zaid.recruitment.Exception.SkillNotFoundException;
import com.zaid.recruitment.Repository.CandidateRepository;
import com.zaid.recruitment.Repository.SkillRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CandidateService  {
    CandidateRepository candidateRepository;
    SkillRepository skillRepository;

    public CandidateService(CandidateRepository candidateRepository, SkillRepository skillRepository) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    public Message addCandidate(CandidateRequestDto candidateRequestDto) {
        Candidate candidate = new Candidate();
        //candidate.setName(candidateRequestDto.getName());
        candidate.setEmail(candidateRequestDto.getEmail());
        candidate.setPhone(candidateRequestDto.getPhone());
        candidate.setAddress(candidateRequestDto.getAddress());
    //    candidate.setPassword(candidateRequestDto.getPassword());
        candidate.setExperience(candidateRequestDto.getExperience());
        candidate.setResumeUrl(candidateRequestDto.getResumeUrl());
       // candidate.setCreatedAt(candidateRequestDto.getCreatedAt());
        List<Long> skills= candidateRequestDto.getSkillIds();
        List<Skills> skillsList = new ArrayList<>();
        for (Long skill : skills) {
            Skills skills1=skillRepository.findById(skill).orElseThrow(()->new SkillNotFoundException("skill does not exists"));
            skillsList.add(skills1);
        }
        candidate.setSkills(skillsList);

        candidateRepository.save(candidate);
        return new Message("Candidate added successfully");
    }

    public Message updateCandidate(CandidateRequestDto candidateRequestDto,long id,Authentication authentication) {
        String authName=authentication.getName();

        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));
       boolean checkName= candidate.getUserEntity().getName().equals(authName);
        if (checkName) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You cannot delete another candidate's profile");
        }

        // candidate.setName(candidateRequestDto.getName());
        candidate.setEmail(candidateRequestDto.getEmail());
        candidate.setPhone(candidateRequestDto.getPhone());
        candidate.setAddress(candidateRequestDto.getAddress());
        candidate.setExperience(candidateRequestDto.getExperience());
        candidate.setResumeUrl(candidateRequestDto.getResumeUrl());
        candidate.setCreatedAt(LocalDateTime.now());
       // candidate.setPassword(candidateRequestDto.getPassword());
        List<Long> skills= candidateRequestDto.getSkillIds();
        List<Skills> skillsList = new ArrayList<>();
        for (Long skill : skills) {
            Skills skills1=skillRepository.findById(skill).orElseThrow(()->new SkillNotFoundException("skill does not exists"));
            skillsList.add(skills1);
        }
        candidate.setSkills(skillsList);
        candidateRepository.save(candidate);
        return new Message("Candidate updated successfully");
    }
    public Message deleteCandidate(long id, Authentication authentication) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String loggedInUsername = authentication.getName();

        boolean isRecruiter = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));

        boolean isOwner = candidate.getUserEntity().getName()
                .equals(loggedInUsername);

        if (!isRecruiter && !isOwner) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You cannot delete another candidate's profile");
        }

        candidateRepository.delete(candidate);

        return new Message("Candidate deleted successfully");
    }
    public CandidateResponceDto getCandidate(long id) {
        Candidate candidate= candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException("candidate not found"));
        CandidateResponceDto candidateResponceDto = new CandidateResponceDto();
        candidateResponceDto.setId(candidate.getId());
       // candidateResponceDto.setName(candidate.getName());
        candidateResponceDto.setEmail(candidate.getEmail());
        candidateResponceDto.setPhone(candidate.getPhone());
        candidateResponceDto.setAddress(candidate.getAddress());
        candidateResponceDto.setExperience(candidate.getExperience());
        candidateResponceDto.setResumeUrl(candidate.getResumeUrl());
        candidateResponceDto.setCreatedAt(candidate.getCreatedAt());
        List<Skills> skill=candidate.getSkills();
        List<Long> skillIds = new ArrayList<>();
        for (Skills skill1: skill) {
            skillIds.add(skill1.getId());
        }
        candidateResponceDto.setSkillIds(skillIds);
        return candidateResponceDto;
    }
    public List<CandidateResponceDto> getCandidates() {

        List<Candidate> candidate= candidateRepository.findAll();
        List<CandidateResponceDto> candidateResponceDto= new ArrayList<CandidateResponceDto>();
        for (Candidate candidate1 : candidate) {
            CandidateResponceDto candidateResponceDto1 = new CandidateResponceDto();
            candidateResponceDto1.setId(candidate1.getId());
          //  candidateResponceDto1.setName(candidate1.getName());
            candidateResponceDto1.setEmail(candidate1.getEmail());
            candidateResponceDto1.setPhone(candidate1.getPhone());
            candidateResponceDto1.setAddress(candidate1.getAddress());
            candidateResponceDto1.setExperience(candidate1.getExperience());
            candidateResponceDto1.setResumeUrl(candidate1.getResumeUrl());
            candidateResponceDto1.setCreatedAt(candidate1.getCreatedAt());
            List<Skills> skill=candidate1.getSkills();
            List<Long> skillIds = new ArrayList<>();
            for (Skills skill1: skill) {
                skillIds.add(skill1.getId());
            }
            candidateResponceDto1.setSkillIds(skillIds);
            candidateResponceDto.add(candidateResponceDto1);

        }
        return candidateResponceDto;
    }
    public CandidateResponceDto getCandidatesByEmail(String email) {

        Candidate candidates= candidateRepository.findByEmail(email).orElseThrow(()->new CandidateNotFoundException("candidate not found"));
        List<CandidateResponceDto> candidateResponceDtos= new ArrayList<>();
        CandidateResponceDto candidateResponceDto= new CandidateResponceDto();
        candidateResponceDto.setId(candidates.getId());
        candidateResponceDto.setEmail(candidates.getEmail());
        candidateResponceDto.setPhone(candidates.getPhone());
        candidateResponceDto.setAddress(candidates.getAddress());
        candidateResponceDto.setExperience(candidates.getExperience());
        candidateResponceDto.setResumeUrl(candidates.getResumeUrl());
        candidateResponceDto.setCreatedAt(candidates.getCreatedAt());
        List<Skills> skill=candidates.getSkills();
        List<Long> skillIds = new ArrayList<>();
        for (Skills skill1: skill) {
            skillIds.add(skill1.getId());
        }
        candidateResponceDto.setSkillIds(skillIds);
        return candidateResponceDto;

    }
    public CandidateResponceDto getMyCandidateProfile(String username) {
        CandidateResponceDto candidateResponceDto = new CandidateResponceDto();
        Candidate candidate= candidateRepository.findByUserEntity_Name(username).orElseThrow(() -> new CandidateNotFoundException("candidate not found"));
        candidateResponceDto.setId(candidate.getId());
        candidateResponceDto.setEmail(candidate.getEmail());
        candidateResponceDto.setPhone(candidate.getPhone());
        candidateResponceDto.setAddress(candidate.getAddress());
        candidateResponceDto.setExperience(candidate.getExperience());
        candidateResponceDto.setResumeUrl(candidate.getResumeUrl());
        candidateResponceDto.setCreatedAt(candidate.getCreatedAt());
        List<Skills> skill=candidate.getSkills();
        List<Long> skillIds = new ArrayList<>();
        for (Skills skill1: skill) {
            skillIds.add(skill1.getId());
        }
        candidateResponceDto.setSkillIds(skillIds);
        return candidateResponceDto;
    }


}

//Can use this method to get list Skills from list skilid instead of repeating code
/*(
private List<Skills> getSkillsFromIds(List<Long> skillIds) {

    List<Skills> skills = new ArrayList<>();

    if (skillIds == null) {
        return skills;
    }

    for (Long skillId : skillIds) {
        Skills skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new SkillNotFoundException("Skill does not exist"));

        skills.add(skill);
    }

    return skills;
})*/