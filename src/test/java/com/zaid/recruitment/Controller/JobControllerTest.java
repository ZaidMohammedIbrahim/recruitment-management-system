package com.zaid.recruitment.Controller;

import com.zaid.recruitment.Auth.JwtService;
import com.zaid.recruitment.DTO.JobRequestDto;
import com.zaid.recruitment.DTO.JobResponseDto;
import com.zaid.recruitment.Service.JobService;
import org.springframework.context.annotation.Import;
import com.zaid.recruitment.Auth.SecurityConfig;
import com.zaid.recruitment.Auth.UserService;


import com.zaid.recruitment.Service.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(JobController.class)
@AutoConfigureMockMvc(addFilters = false)

class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

   @MockitoBean
   JwtService jwtService;


    @Test
    void shouldGetJobById() throws Exception {

        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setId(1L);
        jobResponseDto.setLocation("location");
        jobResponseDto.setEmploymentType("FullTime");

        when(jobService.getJob(1L)).thenReturn(jobResponseDto);

        mockMvc.perform(get("/job/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.location").value("location"))
                .andExpect(jsonPath("$.employmentType").value("FullTime"));

        verify(jobService, times(1)).getJob(1L);
    }

    @Test
    void shouldGetJobs() throws Exception {

        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setId(1L);
        jobResponseDto.setLocation("location");
        jobResponseDto.setEmploymentType("FullTime");

        JobResponseDto jobResponseDto1 = new JobResponseDto();
        jobResponseDto1.setId(2L);
        jobResponseDto1.setLocation("location1");
        jobResponseDto1.setEmploymentType("FullTime1");

        when(jobService.getJobs()).thenReturn(List.of(jobResponseDto, jobResponseDto1));
        mockMvc.perform(get("/job/getAll")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.length()").value(2)).
                andExpect(jsonPath("$[0].id").value(1)).
                andExpect(jsonPath("$[0].location").value("location")).
                andExpect(jsonPath("$[0].employmentType").value("FullTime")).
                andExpect(jsonPath("$[1].id").value(2)).
                andExpect(jsonPath("$[1].location").value("location1")).
                andExpect(jsonPath("$[1].employmentType").value("FullTime1"));
        verify(jobService, times(1)).getJobs();
    }

    @Test
    void shouldGetJobsByLocation() throws Exception {

        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setId(1L);
        jobResponseDto.setLocation("Hyderabad");
        jobResponseDto.setEmploymentType("FullTime");

        JobResponseDto jobResponseDto1 = new JobResponseDto();
        jobResponseDto1.setId(2L);
        jobResponseDto1.setLocation("Hyderabad");
        jobResponseDto1.setEmploymentType("FullTime1");

        when(jobService.getJobsByLocation("Hyderabad")).thenReturn(List.of(jobResponseDto, jobResponseDto1));
        mockMvc.perform(get("/job/getByLocation").param("location", "Hyderabad")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.length()").value(2)).
                andExpect(jsonPath("$[0].id").value(1)).
                andExpect(jsonPath("$[0].location").value("Hyderabad")).
                andExpect(jsonPath("$[0].employmentType").value("FullTime")).
                andExpect(jsonPath("$[1].id").value(2)).
                andExpect(jsonPath("$[1].location").value("Hyderabad")).
                andExpect(jsonPath("$[1].employmentType").value("FullTime1"));
        verify(jobService, times(1)).getJobsByLocation("Hyderabad");
    }

    @Test
    void shouldGetJobsByTitle() throws Exception {

        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setId(1L);
        jobResponseDto.setLocation("Hyderabad");
        jobResponseDto.setEmploymentType("FullTime");
        jobResponseDto.setTitle("Software Engineer");


        JobResponseDto jobResponseDto1 = new JobResponseDto();
        jobResponseDto1.setId(2L);
        jobResponseDto1.setLocation("Secunderabad");
        jobResponseDto1.setEmploymentType("FullTime1");
        jobResponseDto1.setTitle("Software Engineer");

        when(jobService.getJobsByJobTitle("Software Engineer")).thenReturn(List.of(jobResponseDto, jobResponseDto1));
        mockMvc.perform(get("/job/getByTitle/Software Engineer")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.length()").value(2)).
                andExpect(jsonPath("$[0].id").value(1)).
                andExpect(jsonPath("$[0].location").value("Hyderabad")).
                andExpect(jsonPath("$[0].employmentType").value("FullTime")).
                andExpect(jsonPath("$[0].title").value("Software Engineer")).
                andExpect(jsonPath("$[1].id").value(2)).
                andExpect(jsonPath("$[1].location").value("Secunderabad")).
                andExpect(jsonPath("$[1].employmentType").value("FullTime1")).
                andExpect(jsonPath("$[1].title").value("Software Engineer"));

        verify(jobService, times(1)).getJobsByJobTitle("Software Engineer");
    }

    @Test
    void shouldAddJob() throws Exception {

        Message message = new Message("Job has been added successfully");
        when(jobService.addJob(any(JobRequestDto.class)))
                .thenReturn(message);

        mockMvc.perform(post("/job/add").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "title": "Java Developer",
                  "location": "Hyderabad",
                  "employmentType": "FullTime"
                }
                """)).andExpect(status().isCreated()).andExpect(jsonPath("$.message").value("Job has been added successfully"));
    }

    @Test
    void  shouldDeleteJob() throws Exception {
        Message message = new Message("Job has been deleted successfully");
        when(jobService.deleteJob(1L)).thenReturn(message);
        mockMvc.perform(delete("/job/delete/1")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Job has been deleted successfully"));
        verify(jobService, times(1)).deleteJob(1L);
    }



// here it is not actually checking the PreAuthorize condition
    @Test
    @WithMockUser(username = "recruiter", roles = "RECRUITER")
    void shouldAllowAuthenticatedUserToAccessGetAllJobs() throws Exception {

        when(jobService.getJobs()).thenReturn(List.of());

        mockMvc.perform(get("/job/getAll"))
                .andExpect(status().isOk());

        verify(jobService).getJobs();
    }

}