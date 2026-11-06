package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.DTO.SkillsRequestDto;
import com.zaid.recruitment.DTO.SkillsResponseDto;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Service.Message;
import com.zaid.recruitment.Service.SkillsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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
@AutoConfigureMockMvc(addFilters = false)
public class SkillsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    SkillsService skillsService;
    @MockitoBean
    JwtService jwtService;
    @Test
    void shouldGetById() throws Exception {
        SkillsResponseDto skillsResponseDto = new SkillsResponseDto();
        skillsResponseDto.setId(1L);
        skillsResponseDto.setName("name");
        skillsResponseDto.setDescription("description");
        when(skillsService.findSkillById(1L)).thenReturn(skillsResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/getById/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("name")).andExpect(jsonPath("$.description").value("description"));
        verify(skillsService).findSkillById(1L);

    }
    @Test
    void shouldGetByName() throws Exception {
        SkillsResponseDto skillsResponseDto = new SkillsResponseDto();
        skillsResponseDto.setId(1L);
        skillsResponseDto.setName("name");
        skillsResponseDto.setDescription("description");
        SkillsResponseDto skillsResponseDto2 = new SkillsResponseDto();
        skillsResponseDto2.setId(2L);
        skillsResponseDto2.setName("name");
        skillsResponseDto2.setDescription("description2");
        when(skillsService.findBySkillName("name")).thenReturn(Arrays.asList(skillsResponseDto, skillsResponseDto2));
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/getByName").param("name","name")).andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].name").value("name")).
                andExpect(jsonPath("$[0].description").value("description")).
        andExpect(jsonPath("$[1].id").value(2)).
        andExpect(jsonPath("$[1].name").value("name")).
        andExpect(jsonPath("$[1].description").value("description2"));
        verify(skillsService).findBySkillName("name");

    }
    @Test
    void shouldAddSkill() throws Exception {
        SkillsResponseDto skillsResponseDto = new SkillsResponseDto();
        skillsResponseDto.setId(1L);
        skillsResponseDto.setName("name");
        skillsResponseDto.setDescription("description");
        Message message = new Message("Skill added successfully");
        when(skillsService.addSkill(any(SkillsRequestDto.class))).thenReturn(message);
        mockMvc.perform((post("/skills/add").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "name": "name",
                "description": "description"
                
                }"""))).andExpect(status().isCreated()).andExpect(jsonPath("$.message").value("Skill added successfully"));
        verify(skillsService).addSkill(argThat(dto ->
                dto.getName().equals("name")
                        && dto.getDescription().equals("description")
        ));
    }

    @Test
    void shouldDeleteSkill() throws Exception {
    Message message = new Message("Skill deleted successfully");
    when(skillsService.deleteSkill(1L)).thenReturn(message);
    mockMvc.perform(delete("/skills/delete/1")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Skill deleted successfully"));
    verify(skillsService).deleteSkill(1L);
    }
}
