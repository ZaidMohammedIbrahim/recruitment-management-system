package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.SkillsRequestDto;
import com.zaid.recruitment.DTO.SkillsResponseDto;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Exception.SkillNotFoundException;
import com.zaid.recruitment.Repository.SkillRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillsService {
    SkillRepository skillRepository;
    public SkillsService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    public Message addSkill(SkillsRequestDto skillRequestDto) {
        Skills skills = new Skills();
        BeanUtils.copyProperties(skillRequestDto, skills);
        skillRepository.save(skills);
        return new Message("Skill added successfully");
    }
    public Message deleteSkill(long id) {
        Skills skills = skillRepository.findById(id)
                .orElseThrow(() ->
                        new SkillNotFoundException("Skill does not exist"));

        skillRepository.delete(skills);

        return new Message("Skill deleted successfully");
    }
    public Message updateSkill(SkillsRequestDto skillRequestDto, long id) {
        Skills skills=skillRepository.findById(id).orElseThrow(()-> new SkillNotFoundException("skill does not exists"));
        BeanUtils.copyProperties(skillRequestDto, skills);
        skillRepository.save(skills);
        return new Message("Skill updated successfully");
    }
    public SkillsResponseDto getSkillResponseDto(Skills skills) {
        SkillsResponseDto skillsResponseDto = new SkillsResponseDto();
        BeanUtils.copyProperties(skills, skillsResponseDto);
        return skillsResponseDto;
    }
    public SkillsResponseDto findSkillById(long id){
        Skills skills= skillRepository.findById(id).orElseThrow(()-> new SkillNotFoundException("skill does not exists"));
        SkillsResponseDto skillsResponseDto = getSkillResponseDto(skills);
        return skillsResponseDto;
    }
    public List<SkillsResponseDto> findAllSkills(){
        List<Skills> skills= skillRepository.findAll();
        List<SkillsResponseDto> skillsResponseDto = new ArrayList<>();
        for (Skills skill : skills) {
            SkillsResponseDto skillsResponseDto1 = getSkillResponseDto(skill);
            skillsResponseDto.add(skillsResponseDto1);

        }
        return skillsResponseDto;
    }
    public List<SkillsResponseDto> findBySkillName(String skillName){
        List<Skills> skills=skillRepository.findSkillsByName(skillName);
        List<SkillsResponseDto> skillsResponseDto = new ArrayList<>();
        for (Skills skill : skills) {
            SkillsResponseDto skillsResponseDto1 = getSkillResponseDto(skill);
            skillsResponseDto.add(skillsResponseDto1);
        }
        return skillsResponseDto;
    }
}
