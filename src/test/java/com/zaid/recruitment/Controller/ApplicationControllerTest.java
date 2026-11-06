package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.Auth.SecurityConfig;
import com.zaid.recruitment.Auth.UserService;
import com.zaid.recruitment.DTO.ApplicationResponseDto;
import com.zaid.recruitment.Service.ApplicationService;
import com.zaid.recruitment.Service.Message;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
@Import(SecurityConfig.class)
public class ApplicationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    UserService userService;// Creates mock UserService but also registers it as bean in spring application context unlike mock which just creates mock obj but doesn't register as bean
    @MockitoBean
    ApplicationService applicationService;

    @Test
    void shouldGetCandidateApplicationById() throws Exception {
        ApplicationResponseDto applicationResponseDto=new ApplicationResponseDto();
        applicationResponseDto.setCandidateId(1);
        applicationResponseDto.setCandidateName("Zaid");
        applicationResponseDto.setId(1);
        applicationResponseDto.setJobName("Software Testing");
        ApplicationResponseDto applicationResponseDto1=new ApplicationResponseDto();
        applicationResponseDto1.setCandidateId(1);
        applicationResponseDto1.setCandidateName("Zaid");
        applicationResponseDto1.setId(2);
        applicationResponseDto1.setJobName("Software Development");
        String token="Random_Token";
        Claims claims= Jwts.claims().subject("Recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(applicationService.getApplicationsByCandidateId(1)).thenReturn(List.of(applicationResponseDto,applicationResponseDto1));
        mockMvc.perform(get("/application/getByCandidateId/1").header("Authorization","Bearer "+token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(1)).
                andExpect(jsonPath("$[0].candidateName").value("Zaid"))
                .andExpect(jsonPath("$[0].jobName").value("Software Testing")).
                andExpect(jsonPath("$[1].id").value(2)).
                andExpect(jsonPath("$[1].candidateName").value("Zaid")).
                andExpect(jsonPath("$[1].jobName").value("Software Development")).
                andExpect(jsonPath("$[0].candidateId").value(1)).
                andExpect(jsonPath("$[1].candidateId").value(1));
        verify(applicationService).getApplicationsByCandidateId(1);
    }

    @Test
    void shouldNotGetCandidateApplicationByIdIfNotRecruiter() throws Exception {
        ApplicationResponseDto applicationResponseDto=new ApplicationResponseDto();
        applicationResponseDto.setCandidateId(1);
        applicationResponseDto.setCandidateName("Zaid");
        applicationResponseDto.setId(1);
        applicationResponseDto.setJobName("Software Testing");
        ApplicationResponseDto applicationResponseDto1=new ApplicationResponseDto();
        applicationResponseDto1.setCandidateId(1);
        applicationResponseDto1.setCandidateName("Zaid");
        applicationResponseDto1.setId(2);
        applicationResponseDto1.setJobName("Software Development");
        String token="Random_Token";
        Claims claims= Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(applicationService.getApplicationsByCandidateId(1)).thenReturn(List.of(applicationResponseDto,applicationResponseDto1));
        mockMvc.perform(get("/application/getByCandidateId/1").header("Authorization","Bearer "+token)).
                andExpect(status().isForbidden());
        verify(applicationService,never()).getApplicationsByCandidateId(1);
    }

    @Test
    void shouldGetCandidateApplicationByJobId() throws Exception {
        ApplicationResponseDto applicationResponseDto=new ApplicationResponseDto();
        applicationResponseDto.setCandidateId(1);
        applicationResponseDto.setCandidateName("Zaid");
        applicationResponseDto.setId(1);
        applicationResponseDto.setJobId(233);
        applicationResponseDto.setJobName("Software Testing");
        ApplicationResponseDto applicationResponseDto1=new ApplicationResponseDto();
        applicationResponseDto1.setCandidateId(2);
        applicationResponseDto1.setCandidateName("Mohammed");
        applicationResponseDto1.setId(2);
        applicationResponseDto1.setJobId(233);
        applicationResponseDto1.setJobName("Software Testing");
        String token="Random_Token";
        Claims claims= Jwts.claims().subject("Recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(applicationService.getApplicationsByJob(233)).thenReturn(List.of(applicationResponseDto,applicationResponseDto1));
        mockMvc.perform(get("/application/getByJobId/233").header("Authorization","Bearer "+token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(1)).
                andExpect(jsonPath("$[0].candidateName").value("Zaid"))
                .andExpect(jsonPath("$[0].jobName").value("Software Testing")).
                andExpect(jsonPath("$[1].id").value(2)).
                andExpect(jsonPath("$[1].candidateName").value("Mohammed")).
                andExpect(jsonPath("$[1].jobName").value("Software Testing")).
                andExpect(jsonPath("$[0].candidateId").value(1)).
                andExpect(jsonPath("$[1].candidateId").value(2)).
        andExpect(jsonPath("$[0].jobId").value(233)).andExpect(jsonPath("$[1].jobId").value(233));
        verify(applicationService).getApplicationsByJob(233);
    }

    @Test
    void shouldNotGetCandidateApplicationByJobIdIfNotRecruiter() throws Exception {
        ApplicationResponseDto applicationResponseDto=new ApplicationResponseDto();
        applicationResponseDto.setCandidateId(1);
        applicationResponseDto.setCandidateName("Zaid");
        applicationResponseDto.setId(1);
        applicationResponseDto.setJobId(233);
        applicationResponseDto.setJobName("Software Testing");
        ApplicationResponseDto applicationResponseDto1=new ApplicationResponseDto();
        applicationResponseDto1.setCandidateId(2);
        applicationResponseDto1.setCandidateName("Mohammed");
        applicationResponseDto1.setId(2);
        applicationResponseDto1.setJobId(233);
        applicationResponseDto1.setJobName("Software Testing");
        String token="Random_Token";
        Claims claims= Jwts.claims().subject("Candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(applicationService.getApplicationsByJob(233)).thenReturn(List.of(applicationResponseDto,applicationResponseDto1));
        mockMvc.perform(get("/application/getByJobId/233").header("Authorization","Bearer "+token)).
                andExpect(status().isForbidden());
        verify(applicationService,never()).getApplicationsByJob(233);
    }

    @Test
    void shouldGetOwnCandidateApplication() throws Exception {
        ApplicationResponseDto applicationResponseDto=new ApplicationResponseDto();
        applicationResponseDto.setCandidateId(1);
        applicationResponseDto.setCandidateName("Zaid");
        applicationResponseDto.setId(1);
        applicationResponseDto.setJobId(233);
        applicationResponseDto.setJobName("Software Testing");

        String token="Random_Token";
        Claims claims= Jwts.claims().subject("Candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(applicationService.getMe(any(Authentication.class))).thenReturn(applicationResponseDto);
        mockMvc.perform(get("/application/me").header("Authorization","Bearer "+token)).
                andExpect(status().isOk()).andExpect(jsonPath("$.candidateName").value("Zaid")).andExpect(jsonPath("$.id").value(1));
        verify(applicationService).getMe(any(Authentication.class));


    }

    @Test
    void shouldGetAllCandidateApplicationsIfRecruiter() throws Exception {
        ApplicationResponseDto applicationResponseDto=new ApplicationResponseDto();
        applicationResponseDto.setCandidateId(1);
        applicationResponseDto.setCandidateName("Zaid");
        applicationResponseDto.setId(1);
        applicationResponseDto.setJobId(233);
        applicationResponseDto.setJobName("Software Testing");
        ApplicationResponseDto applicationResponseDto1=new ApplicationResponseDto();
        applicationResponseDto1.setCandidateId(2);
        applicationResponseDto1.setCandidateName("Mohammed");
        applicationResponseDto1.setId(2);
        applicationResponseDto1.setJobId(233);
        applicationResponseDto1.setJobName("Software Testing");
        String token="Random_Token";
        Claims claims= Jwts.claims().subject("Recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(applicationService.getApplications()).thenReturn(List.of(applicationResponseDto,applicationResponseDto1));
        mockMvc.perform(get("/application/getAll").header("Authorization","Bearer "+token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(1)).
                andExpect(jsonPath("$[0].candidateName").value("Zaid"))
                .andExpect(jsonPath("$[0].jobName").value("Software Testing")).
                andExpect(jsonPath("$[1].id").value(2)).
                andExpect(jsonPath("$[1].candidateName").value("Mohammed")).
                andExpect(jsonPath("$[1].jobName").value("Software Testing")).
                andExpect(jsonPath("$[0].candidateId").value(1)).
                andExpect(jsonPath("$[1].candidateId").value(2)).
                andExpect(jsonPath("$[0].jobId").value(233)).andExpect(jsonPath("$[1].jobId").value(233));
        verify(applicationService).getApplications();

    }

    @Test
    void deleteCandidateApplication() throws Exception {
        long id=1;
        String token="Random_Token";
        Claims claims=Jwts.claims().subject("Candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        Message message=new Message("Application deleted successfully");
        when(applicationService.deleteApplication(eq(id),any(Authentication.class))).thenReturn(message);
        mockMvc.perform(delete("/application/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Application deleted successfully"));

    verify(applicationService).deleteApplication(eq(id),any(Authentication.class));}


    @Test
    void deleteCandidateApplicationIfRecruiter() throws Exception {
        long id=1;
        String token="Random_Token";
        Claims claims=Jwts.claims().subject("Recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        Message message=new Message("Application deleted successfully");
        when(applicationService.deleteApplication(eq(id),any(Authentication.class))).thenReturn(message);
        mockMvc.perform(delete("/application/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Application deleted successfully"));

        verify(applicationService).deleteApplication(eq(id),any(Authentication.class));}



}
