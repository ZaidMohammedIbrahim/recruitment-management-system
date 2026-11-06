package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.Auth.SecurityConfig;
import com.zaid.recruitment.Auth.UserService;
import com.zaid.recruitment.DTO.SkillsRequestDto;
import com.zaid.recruitment.DTO.SkillsResponseDto;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Service.Message;
import com.zaid.recruitment.Service.SkillsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import static org.mockito.Mockito.*;


//import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static java.nio.file.Paths.get;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;


@WebMvcTest(SkillsController.class)
@Import(SecurityConfig.class)
public class SkillsControllerTestWithAuth {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    SkillsService skillsService;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    private UserService userService;

    @Test
    void shouldDeleteSkillsIfRecruiter() throws Exception {
        String token="Random_test";
        Claims claims= Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(delete("/skills/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isOk());
        verify(skillsService).deleteSkill(1L);
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);
    }
    @Test
    void shouldNotDeleteSkillsIfCandidate() throws Exception {
        String token="Random_test";
        Claims claims= Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        mockMvc.perform(delete("/skills/delete/1").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        verify(skillsService,never()).deleteSkill(1L);
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);
    }

    @Test
    void shouldGetAllSkillsIfRecruiter() throws Exception {
        String token="Random_test";
        Claims claims= Jwts.claims().subject("recruiter").add("role","RECRUITER").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        SkillsResponseDto skillsResponseDto = new SkillsResponseDto();
        skillsResponseDto.setId(1L);
        skillsResponseDto.setName("name");
        skillsResponseDto.setDescription("description");
        SkillsResponseDto skillsResponseDto2 = new SkillsResponseDto();
        skillsResponseDto2.setId(2L);
        skillsResponseDto2.setName("name2");
        skillsResponseDto2.setDescription("description2");
        when(skillsService.findAllSkills()).thenReturn(Arrays.asList(skillsResponseDto, skillsResponseDto2));
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/getAll").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].name").value("name")).andExpect(jsonPath("$[0].description").value("description")).andExpect(jsonPath("$[1].id").value(2)).andExpect(jsonPath("$[1].name").value("name2")).andExpect(jsonPath("$[1].description").value("description2"));
        verify(skillsService).findAllSkills();
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);

    }
    @Test
    void shouldNotGetAllSkillsIfCandidate() throws Exception {
        String token="Random_test";
        Claims claims= Jwts.claims().subject("candidate").add("role","CANDIDATE").build();
        when(jwtService.parseToken(token)).thenReturn(claims);
        when(jwtService.validateToken(claims)).thenReturn(true);
        SkillsResponseDto skillsResponseDto = new SkillsResponseDto();
        skillsResponseDto.setId(1L);
        skillsResponseDto.setName("name");
        skillsResponseDto.setDescription("description");
        SkillsResponseDto skillsResponseDto2 = new SkillsResponseDto();
        skillsResponseDto2.setId(2L);
        skillsResponseDto2.setName("name2");
        skillsResponseDto2.setDescription("description2");
        when(skillsService.findAllSkills()).thenReturn(Arrays.asList(skillsResponseDto, skillsResponseDto2));
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/getAll").header("Authorization","Bearer "+token)).andExpect(status().isForbidden());
        verify(skillsService,never()).findAllSkills();
        verify(jwtService).parseToken(token);
        verify(jwtService).validateToken(claims);

    }

}