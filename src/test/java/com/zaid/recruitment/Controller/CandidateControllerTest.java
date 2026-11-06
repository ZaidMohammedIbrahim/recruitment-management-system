package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.Auth.SecurityConfig;
import com.zaid.recruitment.Auth.UserService;
import com.zaid.recruitment.DTO.CandidateResponceDto;
import com.zaid.recruitment.Service.CandidateService;
import com.zaid.recruitment.Service.Message;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;




//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers=CandidateController.class)
@Import(SecurityConfig.class)
public class CandidateControllerTest {
    @MockitoBean
    private CandidateService candidateService;
    @MockitoBean
    JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    UserService userService;

    @Test
    void getCandidates() throws Exception {
        CandidateResponceDto candidateResponceDto=new CandidateResponceDto();
        candidateResponceDto.setName("name");
        candidateResponceDto.setId(1);
        candidateResponceDto.setResumeUrl("resumeUrl");
        CandidateResponceDto candidateResponceDto2=new CandidateResponceDto();
        candidateResponceDto2.setName("name2");
        candidateResponceDto2.setId(2);
        candidateResponceDto2.setResumeUrl("resumeUrl2");
        when(candidateService.getCandidates()).thenReturn(Arrays.asList(candidateResponceDto, candidateResponceDto2));
        String token="Random_test";
        Claims claims= Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(get("/candidate/getAll").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].name").value("name")).andExpect(jsonPath("$[0].resumeUrl").value("resumeUrl"));
        verify(candidateService).getCandidates();
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);
    }
    @Test
    void shouldGetCandidateById() throws Exception {
        CandidateResponceDto candidateResponceDto=new CandidateResponceDto();
        candidateResponceDto.setName("name");
        candidateResponceDto.setId(1);
        candidateResponceDto.setResumeUrl("resumeUrl");
        when(candidateService.getCandidate(1)).thenReturn(candidateResponceDto);
        String token="Random_test";
        Claims claims= Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(get("/candidate/getById/1").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("name")).andExpect(jsonPath("$.resumeUrl").value("resumeUrl"));
        verify(candidateService).getCandidate(1);
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);

    }

    @Test
    void shouldGetCandidateByEmail() throws Exception {

        CandidateResponceDto candidateResponceDto=new CandidateResponceDto();
        candidateResponceDto.setName("name");
        candidateResponceDto.setId(1);
        candidateResponceDto.setResumeUrl("resumeUrl");
        candidateResponceDto.setEmail("email");
        when(candidateService.getCandidatesByEmail("email")).thenReturn(candidateResponceDto);
        String token="Random_test";
        Claims claims= Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(get("/candidate/getByEmail").param("email","email").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("name")).andExpect(jsonPath("$.email").value("email"));
        verify(candidateService).getCandidatesByEmail("email");
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);

    }
    @Test
    void shouldGetCandidateUser() throws Exception {
        CandidateResponceDto candidateResponceDto=new CandidateResponceDto();
        candidateResponceDto.setName("name");
        candidateResponceDto.setId(1);
        candidateResponceDto.setResumeUrl("resumeUrl");
        when(candidateService.getMyCandidateProfile("candidate")).thenReturn(candidateResponceDto);
        String token="Random_test";
        Claims claims= Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(get("/candidate/me").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("name")).andExpect(jsonPath("$.resumeUrl").value("resumeUrl"));
        verify(candidateService).getMyCandidateProfile("candidate");
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);
    }

    @Test
    void shouldNotGetUserIfRecruiter() throws Exception {
        CandidateResponceDto candidateResponceDto=new CandidateResponceDto();
        candidateResponceDto.setName("name");
        candidateResponceDto.setId(1);
        candidateResponceDto.setResumeUrl("resumeUrl");
        when(candidateService.getMyCandidateProfile("recruiter")).thenReturn(candidateResponceDto);
        String token="Random_test";
        Claims claims= Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(get("/candidate/me").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        verify(candidateService,never()).getMyCandidateProfile("recruiter");
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);
    }


    @Test
    void shouldDeleteByIdIfRecruiter() throws Exception {
        long id=1L;
        Message message=new Message("Candidate deleted successfully");
        String token="Random_test";
        Claims claims=Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(candidateService.deleteCandidate(eq(id),any(Authentication.class))).thenReturn(message);
        mockMvc.perform(delete("/candidate/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Candidate deleted successfully"));
        verify(candidateService).deleteCandidate(eq(id),argThat(argument -> argument.getName().equals("recruiter")&& argument.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_RECRUITER"))));


    }
}
