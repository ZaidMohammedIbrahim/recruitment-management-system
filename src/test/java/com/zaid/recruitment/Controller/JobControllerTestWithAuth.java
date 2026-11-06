package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.Service.JobService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Import;
import com.zaid.recruitment.Auth.SecurityConfig;
import com.zaid.recruitment.Auth.UserService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(JobController.class)
@Import(SecurityConfig.class)

class JobControllerTestWithAuth {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    private UserService userService;




    @Test
    @WithMockUser(roles = "CANDIDATE")
    void shouldDenyCandidateFromDeletingJob() throws Exception {

        String token = "test-token";

        Claims claims = Jwts.claims()
                .subject("candidate")
                .add("role", "CANDIDATE")
                .build();

        when(jwtService.parseToken("test-token")).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);

        mockMvc.perform(delete("/job/delete/1").header("Authorization", "Bearer test-token"))

                .andExpect(status().isForbidden());

        verify(jobService, never()).deleteJob(anyLong());
        verify(jwtService).parseToken("test-token");
        verify(jwtService).validateToken(claims);

    }
    @Test
    void shouldAllowAuthenticatedRecruiterToDeleteJob() throws Exception {
        String token = "test-token";

        Claims claims = Jwts.claims()
                .subject("recruiter")
                .add("role", "RECRUITER")
                .build();

        when(jwtService.parseToken("test-token")).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);

        mockMvc.perform(delete("/job/delete/1")
                        .header("Authorization", "Bearer test-token")).andExpect(status().isOk());


        verify(jwtService).parseToken("test-token");
        verify(jwtService).validateToken(claims);
        verify(jobService).deleteJob(1L);

    }
}