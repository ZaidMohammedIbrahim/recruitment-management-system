package com.zaid.recruitment.Controller;

import com.zaid.recruitment.DTO.SkillsRequestDto;
import com.zaid.recruitment.DTO.SkillsResponseDto;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Service.Message;
import com.zaid.recruitment.Service.SkillsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillsController {
    SkillsService skillsService;
    public SkillsController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }
    @PostMapping("/add")
    public ResponseEntity<Message> addSkills(@RequestBody SkillsRequestDto skillsRequestDto) {
        return new ResponseEntity<>(skillsService.addSkill(skillsRequestDto), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Message> updateSkills(@RequestBody SkillsRequestDto skillsRequestDto, @PathVariable long id) {
        return new ResponseEntity<>(skillsService.updateSkill(skillsRequestDto,id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Message> deleteSkills(@PathVariable("id") long id) {
        return new ResponseEntity<>(skillsService.deleteSkill(id), HttpStatus.OK);
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<SkillsResponseDto> getSkillById(@PathVariable("id") long id) {
        return new ResponseEntity<>(skillsService.findSkillById(id), HttpStatus.OK);
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<SkillsResponseDto>> getAllSkills() {
        return new ResponseEntity<>(skillsService.findAllSkills(), HttpStatus.OK);
    }
    @GetMapping("/getByName")
    public ResponseEntity<List<SkillsResponseDto>> getSkillByName(@RequestParam String name) {
        return new ResponseEntity<>(skillsService.findBySkillName(name), HttpStatus.OK);
    }

}
