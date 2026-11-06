package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.CandidateRequestDto;
import com.zaid.recruitment.DTO.CandidateResponceDto;
import com.zaid.recruitment.Entity.Candidate;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Exception.CandidateNotFoundException;
import com.zaid.recruitment.Exception.SkillNotFoundException;
import com.zaid.recruitment.Repository.CandidateRepository;
import com.zaid.recruitment.Repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;


import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {
    @Mock
    private CandidateRepository candidateRepository;
    @Mock
    private SkillRepository skillRepository;
    @InjectMocks
    private CandidateService candidateService;
    @Test
    void shouldGetSkillsFromRequestDto() {
        CandidateRequestDto candidateRequestDto = new CandidateRequestDto();
        candidateRequestDto.setSkillIds(List.of(1L,2L,3L));
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        Skills skills3 = new Skills();
        skills3.setId(3L);
        skills3.setName("skill3");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skills1));
        when(skillRepository.findById(2L)).thenReturn(Optional.of(skills2));
        when(skillRepository.findById(3L)).thenReturn(Optional.of(skills3));
        List<Skills> skills = candidateService.getSkillsFromRequestDto(candidateRequestDto);
        assertEquals("skill",skills.get(0).getName());
        assertEquals("skill2",skills.get(1).getName());
        assertEquals("skill3",skills.get(2).getName());
        assertEquals(1L,skills.get(0).getId());
        assertEquals(2L,skills.get(1).getId());
        assertEquals(3L,skills.get(2).getId());

        verify(skillRepository, times(1)).findById(1L);
        verify(skillRepository, times(1)).findById(2L);
        verify(skillRepository, times(1)).findById(3L);




    }
    @Test
    void shouldThrowExceptionWhenSkillIdIsNull() {
        CandidateRequestDto candidateRequestDto = new CandidateRequestDto();
        candidateRequestDto.setSkillIds(List.of(1L));
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());
       SkillNotFoundException exception= assertThrows(SkillNotFoundException.class, () -> {candidateService.getSkillsFromRequestDto(candidateRequestDto);});
        assertEquals("skill does not exists",exception.getMessage());
        verify(skillRepository, times(1)).findById(1L);
    }
    @Test
    void shouldGetSkillIdsFromCandidate() {
        Candidate candidate = new Candidate();
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill1");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        candidate.setSkills(List.of(skills1,skills2));
        List<Long> skillIds = candidateService.getSkillIdsFromCandidate(candidate);
        assertEquals(1L,skillIds.get(0));
        assertEquals(2L,skillIds.get(1));
    }
    @Test
    void shouldGetCandidateById(){
        Candidate candidate = new Candidate();
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill1");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        candidate.setSkills(List.of(skills1,skills2));
        candidate.setId(1L);
        candidate.setResumeUrl("resumeUrl");
        candidate.setEmail("email");
        candidate.setPhone("phone");
        candidate.setName("name");
        candidate.setExperience("experience");
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        CandidateResponceDto candidateResponceDto = candidateService.getCandidate(1L);
        assertEquals("resumeUrl",candidateResponceDto.getResumeUrl());
        assertEquals("email",candidateResponceDto.getEmail());
        assertEquals("phone",candidateResponceDto.getPhone());
        assertEquals("name",candidateResponceDto.getName());
        assertEquals("experience",candidateResponceDto.getExperience());
        assertEquals(1L,candidateResponceDto.getId());
        assertEquals(List.of(1L,2L),candidateResponceDto.getSkillIds());
        verify(candidateRepository, times(1)).findById(1L);
    }
    @Test
    void shouldGetCandidatesAll(){

        Candidate candidate = new Candidate();
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill1");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        candidate.setSkills(List.of(skills1,skills2));
        candidate.setId(1L);
        candidate.setResumeUrl("resumeUrl");
        candidate.setEmail("email");
        candidate.setPhone("phone");
        candidate.setName("name");
        candidate.setExperience("experience");
        Candidate candidate1 = new Candidate();
        Skills skills3 = new Skills();
        skills3.setId(3L);
        skills3.setName("skill3");
        candidate1.setSkills(List.of(skills1,skills3));
        candidate1.setId(2L);
        candidate1.setResumeUrl("resumeUrl1");
        candidate1.setEmail("email1");
        candidate1.setPhone("phone1");
        candidate1.setName("name1");
        candidate1.setExperience("experience1");
        when(candidateRepository.findAll()).thenReturn(List.of(candidate,candidate1));
        List<CandidateResponceDto> candidateResponceDto = candidateService.getCandidates();
        assertEquals("resumeUrl",candidateResponceDto.get(0).getResumeUrl());
        assertEquals("email",candidateResponceDto.get(0).getEmail());
        assertEquals("phone",candidateResponceDto.get(0).getPhone());
        assertEquals("name",candidateResponceDto.get(0).getName());
        assertEquals("experience",candidateResponceDto.get(0).getExperience());
        assertEquals(1L,candidateResponceDto.get(0).getId());
        assertEquals(List.of(1L,2L),candidateResponceDto.get(0).getSkillIds());

        assertEquals("resumeUrl1",candidateResponceDto.get(1).getResumeUrl());
        assertEquals("email1",candidateResponceDto.get(1).getEmail());
        assertEquals("phone1",candidateResponceDto.get(1).getPhone());
        assertEquals("name1",candidateResponceDto.get(1).getName());
        assertEquals("experience1",candidateResponceDto.get(1).getExperience());
        assertEquals(2L,candidateResponceDto.get(1).getId());
        assertEquals(List.of(1L,3L),candidateResponceDto.get(1).getSkillIds());

        verify(candidateRepository, times(1)).findAll();

    }
    @Test
    void shouldGetCandidateUser(){

        Candidate candidate = new Candidate();
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill1");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        candidate.setSkills(List.of(skills1,skills2));
        candidate.setId(1L);
        candidate.setResumeUrl("resumeUrl");
        candidate.setEmail("email");
        candidate.setPhone("phone");
        candidate.setName("name");
        candidate.setExperience("experience");
        when(candidateRepository.findByUserEntity_Name(candidate.getName())).thenReturn(Optional.of(candidate));
        CandidateResponceDto candidateResponceDto = candidateService.getMyCandidateProfile(candidate.getName());
        assertEquals("resumeUrl",candidateResponceDto.getResumeUrl());
        assertEquals("email",candidateResponceDto.getEmail());
        assertEquals("phone",candidateResponceDto.getPhone());
        assertEquals("name",candidateResponceDto.getName());
        assertEquals("experience",candidateResponceDto.getExperience());
        assertEquals(1L,candidateResponceDto.getId());
        assertEquals(List.of(1L,2L),candidateResponceDto.getSkillIds());
        verify(candidateRepository, times(1)).findByUserEntity_Name(candidate.getName());

    }
    @Test
    void shouldGetCandidateByEmail(){
        Candidate candidate = new Candidate();
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill1");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        candidate.setSkills(List.of(skills1,skills2));
        candidate.setId(1L);
        candidate.setResumeUrl("resumeUrl");
        candidate.setEmail("get@email");
        candidate.setPhone("phone");
        candidate.setName("name");
        candidate.setExperience("experience");
        when(candidateRepository.findByEmail("get@email")).thenReturn(Optional.of(candidate));
        CandidateResponceDto candidateResponceDto = candidateService.getCandidatesByEmail("get@email");
        assertEquals("resumeUrl",candidateResponceDto.getResumeUrl());
        assertEquals("get@email",candidateResponceDto.getEmail());
        assertEquals("phone",candidateResponceDto.getPhone());
        assertEquals("name",candidateResponceDto.getName());
        assertEquals("experience",candidateResponceDto.getExperience());
        assertEquals(1L,candidateResponceDto.getId());
        assertEquals(List.of(1L,2L),candidateResponceDto.getSkillIds());
        verify(candidateRepository, times(1)).findByEmail(candidate.getEmail());
    }
    @Test
    void shouldAddCandidate(){
        CandidateRequestDto candidateRequestDto = new CandidateRequestDto();
        candidateRequestDto.setName("name");
        candidateRequestDto.setResumeUrl("resumeUrl");
        candidateRequestDto.setEmail("get@email");
        candidateRequestDto.setPhone("phone");
        candidateRequestDto.setSkillIds(List.of(1L,2L));
        Skills skills1 = new Skills();
        skills1.setId(1L);
        skills1.setName("skill1");
        Skills skills2 = new Skills();
        skills2.setId(2L);
        skills2.setName("skill2");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skills1));
        when(skillRepository.findById(2L)).thenReturn(Optional.of(skills2));
        Message message=candidateService.addCandidate(candidateRequestDto);
        assertEquals("Candidate added successfully",message.getMessage());
        verify(candidateRepository, times(1)).save(argThat((candidate)->candidate.getName().equals("name")&&candidate.getResumeUrl().equals("resumeUrl")&&candidate.getEmail().equals("get@email")&&candidate.getPhone().equals("phone") && candidate.getSkills().get(0).getId()==1L && candidate.getSkills().get(1).getId()==2L));
    }

    @Test
    void shouldDeleteOwnCandidateProfile() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("name");
        Authentication authentication=mock(Authentication.class);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(authentication.getName()).thenReturn("name");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_CANDIDATE")))
                .when(authentication).getAuthorities();
        Message message=candidateService.deleteCandidate(candidate.getId(),authentication );
        assertEquals("Candidate deleted successfully",message.getMessage());
        verify(candidateRepository, times(1)).findById(1L);
        verify(candidateRepository,times(1)).delete(candidate);

    }
    @Test
    void shouldAllowRecruiterToDeleteCandidate() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("name");
        Authentication authentication=mock(Authentication.class);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(authentication.getName()).thenReturn("name");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_RECRUITER"))).when(authentication).getAuthorities();
        Message message=candidateService.deleteCandidate(candidate.getId(),authentication);
        assertEquals("Candidate deleted successfully",message.getMessage());
        verify(candidateRepository, times(1)).delete(candidate);
    }
    @Test
    void shouldThrowExceptionWhenCandidateDeletesAnotherCandidatesProfile() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Zaid");
        Authentication authentication=mock(Authentication.class);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(authentication.getName()).thenReturn("name");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_CANDIDATE"))).when(authentication).getAuthorities();
        assertThrows(AccessDeniedException.class,() -> candidateService.deleteCandidate(candidate.getId(),authentication));
        verify(candidateRepository,never()).delete(candidate);
    }
    @Test
    void shouldThrowExceptionWhenCandidateNotFound() {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
       Authentication authentication=mock(Authentication.class);
       when(candidateRepository.findById(1L)).thenReturn(Optional.empty());
       CandidateNotFoundException exception = assertThrows(CandidateNotFoundException.class, () -> candidateService.deleteCandidate(candidate.getId(),authentication));
       assertEquals("Candidate not found",exception.getMessage());
       verify(candidateRepository,times(1)).findById(1L);

    }


}
