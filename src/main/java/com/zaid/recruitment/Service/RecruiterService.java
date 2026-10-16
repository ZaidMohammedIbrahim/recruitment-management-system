package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.RecruiterRequestDto;
import com.zaid.recruitment.DTO.RecruiterResponseDto;
import com.zaid.recruitment.Entity.Recruiter;
import com.zaid.recruitment.Exception.RecruiterNotFoundException;
import com.zaid.recruitment.Repository.RecruiterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecruiterService {
    RecruiterRepository recruiterRepository;
    public RecruiterService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    public Recruiter getRecruiterFromRequestDto(RecruiterRequestDto recruiterRequestDto) {
        Recruiter recruiter = new Recruiter();
        BeanUtils.copyProperties(recruiterRequestDto,recruiter);
        return  recruiter;
    }
    public Message addRecruiter(RecruiterRequestDto recruiterRequestDto) {
        Recruiter recruiter = getRecruiterFromRequestDto(recruiterRequestDto);
        recruiterRepository.save(recruiter);
        return new Message("Recruiter has been saved successfully");
    }
    public Message updateRecruiter(RecruiterRequestDto recruiterRequestDto,long recruiterId) {
        Recruiter recruiter = getRecruiterFromRequestDto(recruiterRequestDto);
        recruiterRepository.findById(recruiterId).orElseThrow(()-> new RecruiterNotFoundException("Recruiter not found"));
        recruiterRepository.save(recruiter);
        return new Message("Recruiter has been updated successfully");
    }
    public Message deleteRecruiter(long recruiter_id) {
        recruiterRepository.deleteById(recruiter_id);
        return new Message("Recruiter has been deleted successfully");
    }


    public RecruiterResponseDto getResponseDtoFromRecruiter(Recruiter recruiter) {
        RecruiterResponseDto recruiterResponseDto = new RecruiterResponseDto();
        BeanUtils.copyProperties(recruiter,recruiterResponseDto);
        return recruiterResponseDto;
    }



    public List<RecruiterResponseDto> getAllRecruiter()
    {
        List<Recruiter> recruiter= recruiterRepository.findAll();
        List<RecruiterResponseDto> recruiterResponseDto = new ArrayList<>();
        for (Recruiter recruiter1 : recruiter) {
            RecruiterResponseDto recruiterResponseDto1 = new RecruiterResponseDto();
            BeanUtils.copyProperties(recruiter1,recruiterResponseDto1);
            recruiterResponseDto.add(recruiterResponseDto1);
        }
        return recruiterResponseDto;

    }
    public List<RecruiterResponseDto> findByEmail(String email) {
        List<Recruiter> recruiter=recruiterRepository.findByEmail(email);
        List<RecruiterResponseDto> recruiterResponseDto=new ArrayList<>();
        for (Recruiter recruiter1 : recruiter) {
            RecruiterResponseDto recruiterResponseDto1 = new RecruiterResponseDto();
            BeanUtils.copyProperties(recruiter1,recruiterResponseDto1);
            recruiterResponseDto.add(recruiterResponseDto1);
        }
        return recruiterResponseDto;
    }
    public RecruiterResponseDto findByRecruiter_id(long recruiter_id) {
        Recruiter recruiter= recruiterRepository.findById(recruiter_id).orElseThrow(()-> new RecruiterNotFoundException("Recruiter not found"));
        RecruiterResponseDto recruiterResponseDto = getResponseDtoFromRecruiter(recruiter);
        return recruiterResponseDto;
    }
}
