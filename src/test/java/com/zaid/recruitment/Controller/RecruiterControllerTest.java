package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.Auth.SecurityConfig;
import com.zaid.recruitment.Auth.UserService;
import com.zaid.recruitment.DTO.RecruiterResponseDto;
import com.zaid.recruitment.Service.Message;
import com.zaid.recruitment.Service.RecruiterService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecruiterController.class)
@Import(SecurityConfig.class)
public class RecruiterControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    private RecruiterService recruiterService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtService jwtService;
    @Test
    public void shouldGetAllRecruiters () throws Exception {
        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();
        recruiterResponseDto.setName("Recruiter");
        recruiterResponseDto.setId(1);
        RecruiterResponseDto recruiterResponseDto2 = new RecruiterResponseDto();
        recruiterResponseDto2.setName("Recruiter2");
        recruiterResponseDto2.setId(2);
        String token= "Random_Token";
        Claims claims = Jwts.claims().subject("Recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.getAllRecruiter()).thenReturn(Arrays.asList(recruiterResponseDto,recruiterResponseDto2));
        mockMvc.perform(get("/recruiter/getAll").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Recruiter")).andExpect(jsonPath("$[1].name").value("Recruiter2")).andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[1].id").value(2));
         Mockito.verify(recruiterService).getAllRecruiter();
        }


    @Test
    void shouldNotGetRecruiterByEmailIfNotAdmin() throws Exception {
        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();
        recruiterResponseDto.setName("Recruiter");
        recruiterResponseDto.setId(1);
        recruiterResponseDto.setEmail("email");
        String token= "Random_Token";
        Claims claims=Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.findByEmail("email")).thenReturn(recruiterResponseDto);
        mockMvc.perform(get("/recruiter/getByEmail/email").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        Mockito.verify(recruiterService,never()).findByEmail("email");
    }
    @Test
    void shouldNotGetRecruiterByEmailIfCandidateAndNotAdmin() throws Exception {
        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();
        recruiterResponseDto.setName("Recruiter");
        recruiterResponseDto.setId(1);
        recruiterResponseDto.setEmail("email");
        String token= "Random_Token";
        Claims claims=Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.findByEmail("email")).thenReturn(recruiterResponseDto);
        mockMvc.perform(get("/recruiter/getByEmail/email").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        Mockito.verify(recruiterService,never()).findByEmail("email");
    }

    @Test
    void shouldGetRecruiterByEmailIfRecruiter() throws Exception {
        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();
        recruiterResponseDto.setName("Recruiter");
        recruiterResponseDto.setId(1);
        recruiterResponseDto.setEmail("email");
        String token= "Random_Token";
        Claims claims=Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.findByRecruiter_id(1)).thenReturn(recruiterResponseDto);
        mockMvc.perform(get("/recruiter/getById/1").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Recruiter")).andExpect(jsonPath("$.id").value(1));
        Mockito.verify(recruiterService).findByRecruiter_id(1);
    }

    @Test
    void shouldNotGetRecruiterByEmailIfNotRecruiter() throws Exception {
        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();
        recruiterResponseDto.setName("Recruiter");
        recruiterResponseDto.setId(1);
        recruiterResponseDto.setEmail("email");
        String token= "Random_Token";
        Claims claims=Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.findByRecruiter_id(1)).thenReturn(recruiterResponseDto);
        mockMvc.perform(get("/recruiter/getById/1").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        Mockito.verify(recruiterService,never()).findByRecruiter_id(1);
    }

    @Test
    void shouldDeleteRecruiterIfAdmin() throws Exception {
        long id=1;
        Message message=new Message("Recruiter deleted successfully");
        String token= "Random_Token";
        Claims claims=Jwts.claims().subject("Admin").add("role","ADMIN").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.deleteRecruiter(1)).thenReturn(message);
        mockMvc.perform(delete("/recruiter/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Recruiter deleted successfully"));
        Mockito.verify(recruiterService).deleteRecruiter(1);
    }

    @Test
    void shouldNotDeleteRecruiterIfNotAdmin() throws Exception {
        long id=1;
        Message message=new Message("Recruiter deleted successfully");
        String token= "Random_Token";
        Claims claims=Jwts.claims().subject("Recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        when(recruiterService.deleteRecruiter(1)).thenReturn(message);
        mockMvc.perform(delete("/recruiter/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        Mockito.verify(recruiterService,never()).deleteRecruiter(1);
    }

}
