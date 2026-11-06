package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.RecruiterRequestDto;
import com.zaid.recruitment.DTO.RecruiterResponseDto;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Repository.RecruiterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@ExtendWith(MockitoExtension.class)
public class RecruiterServiceTest {
    @Mock
    private RecruiterRepository recruiterRepository;
    @InjectMocks
    private RecruiterService recruiterService;
    @Test
    void shouldConvertRequestDtoToRecruiter() {
        RecruiterRequestDto recruiterRequestDto = new RecruiterRequestDto();
        recruiterRequestDto.setEmail("email");
       // recruiterRequestDto.setName("name");
        recruiterRequestDto.setPhone("phone");
        recruiterRequestDto.setCompanyName("companyName");
        Recruiter recruiter = recruiterService.getRecruiterFromRequestDto(recruiterRequestDto);
        assertEquals("email",recruiter.getEmail());
        assertEquals("phone",recruiter.getPhone());
        assertEquals("companyName",recruiter.getCompanyName());


    }
    @Test
    void shouldConvertRequestDtoToRecruiterThenSave()  {
        RecruiterRequestDto recruiterRequestDto = new RecruiterRequestDto();
        recruiterRequestDto.setEmail("email");
        recruiterRequestDto.setPhone("phone");
        recruiterRequestDto.setCompanyName("companyName");
        Message message=recruiterService.addRecruiter(recruiterRequestDto);
        assertEquals("Recruiter has been saved successfully", message.getMessage());
        verify(recruiterRepository,times(1)).save(argThat(recruiter->recruiter.getEmail().equals("email")&&recruiter.getPhone().equals("phone")&&recruiter.getCompanyName().equals("companyName")));
    }
    @Test
    void shouldDeleteRecruiter() {
        Message message=recruiterService.deleteRecruiter(1L);
        assertEquals("Recruiter has been deleted successfully", message.getMessage());
        verify(recruiterRepository,times(1)).deleteById(1L);
    }
    @Test
    void shouldGetResponseDtoFromRecruiter(){
        Recruiter recruiter=new Recruiter();
        recruiter.setEmail("email");
        recruiter.setPhone("phone");
        recruiter.setCompanyName("companyName");
        RecruiterResponseDto  recruiterResponseDto = recruiterService.getResponseDtoFromRecruiter(recruiter);
        assertEquals("email",recruiterResponseDto.getEmail());
        assertEquals("phone",recruiterResponseDto.getPhone());
        assertEquals("companyName",recruiterResponseDto.getCompanyName());
    }
    @Test
    void shouldFindRecruiterById(){
        Recruiter recruiter=new Recruiter();
        recruiter.setEmail("email");
        recruiter.setPhone("phone");
        recruiter.setCompanyName("companyName");
        when(recruiterRepository.findById(1L)).thenReturn(java.util.Optional.of(recruiter));
        RecruiterResponseDto recruiterResponseDto=recruiterService.findByRecruiter_id(1L);
        assertEquals("email",recruiterResponseDto.getEmail());
        assertEquals("phone",recruiterResponseDto.getPhone());
        assertEquals("companyName",recruiterResponseDto.getCompanyName());
        //assertEquals(1,recruiterResponseDto.getId());
        verify(recruiterRepository,times(1)).findById(1L);
    }
    @Test
    void shouldThrowExceptionWhenRecruiterNotFound() {
        when(recruiterRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        RuntimeException runtimeException=assertThrows(RuntimeException.class, ()->{
            recruiterService.findByRecruiter_id(1L);
        });
        assertEquals("Recruiter not found", runtimeException.getMessage());
        verify(recruiterRepository,times(1)).findById(1L);
    }
    @Test
    void shouldReturnAllRecruiters() {
        Recruiter recruiter1=new Recruiter();
        Recruiter recruiter2=new Recruiter();
        recruiter1.setEmail("email1");
        recruiter1.setPhone("phone1");
        recruiter1.setCompanyName("company1");
        recruiter2.setEmail("email2");
        recruiter2.setPhone("phone2");
        recruiter2.setCompanyName("company2");
        when(recruiterRepository.findAll()).thenReturn(List.of(recruiter1,recruiter2));
        List<RecruiterResponseDto> recruiterResponseDto=recruiterService.getAllRecruiter();
        assertEquals(2,recruiterResponseDto.size());
        assertEquals("email1",recruiterResponseDto.get(0).getEmail());
        assertEquals("phone1",recruiterResponseDto.get(0).getPhone());
        assertEquals("company1",recruiterResponseDto.get(0).getCompanyName());
        assertEquals("email2",recruiterResponseDto.get(1).getEmail());
        assertEquals("phone2",recruiterResponseDto.get(1).getPhone());
        assertEquals("company2",recruiterResponseDto.get(1).getCompanyName());
        verify(recruiterRepository,times(1)).findAll();


    }
    @Test
    void shouldGetRecruiterByEmail() {
        Recruiter recruiter=new Recruiter();
        recruiter.setEmail("email1");
        recruiter.setPhone("phone1");
        recruiter.setCompanyName("company1");
        when(recruiterRepository.findByEmail("email1")).thenReturn(recruiter);
        RecruiterResponseDto recruiterResponseDto=recruiterService.findByEmail("email1");
        assertEquals("email1",recruiterResponseDto.getEmail());
        assertEquals("phone1",recruiterResponseDto.getPhone());
        assertEquals("company1",recruiterResponseDto.getCompanyName());
        verify(recruiterRepository,times(1)).findByEmail("email1");
    }



}
