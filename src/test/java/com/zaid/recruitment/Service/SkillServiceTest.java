
package com.zaid.recruitment.Service;

import com.zaid.recruitment.DTO.SkillsRequestDto;
import com.zaid.recruitment.DTO.SkillsResponseDto;
import com.zaid.recruitment.Entity.Skills;
import com.zaid.recruitment.Exception.SkillNotFoundException;
import com.zaid.recruitment.Repository.SkillRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillsServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillsService skillsService;


    // 1. ADD SKILL

    @Test
    void shouldAddSkill() {

        SkillsRequestDto requestDto = new SkillsRequestDto();
        requestDto.setName("Java");

        Message result = skillsService.addSkill(requestDto);

        assertEquals("Skill added successfully", result.getMessage());

        verify(skillRepository, times(1))
                .save(argThat(skill ->
                        "Java".equals(skill.getName())
                ));
    }


    // 2. DELETE SKILL - SUCCESS

    @Test
    void shouldDeleteSkill() {

        Skills skill = new Skills();
        skill.setId(1L);
        skill.setName("Java");

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        Message result = skillsService.deleteSkill(1L);

        assertEquals("Skill deleted successfully", result.getMessage());

        verify(skillRepository).findById(1L);
        verify(skillRepository).delete(skill);
    }


    // 3. DELETE SKILL - NOT FOUND

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSkill() {

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        SkillNotFoundException exception = assertThrows(
                SkillNotFoundException.class,
                () -> skillsService.deleteSkill(99L)
        );

        assertEquals("Skill does not exist", exception.getMessage());

        verify(skillRepository).findById(99L);
        verify(skillRepository, never()).delete(any(Skills.class));
    }


    // 4. UPDATE SKILL - SUCCESS

    @Test
    void shouldUpdateSkill() {

        Skills existingSkill = new Skills();
        existingSkill.setId(1L);
        existingSkill.setName("Java");

        SkillsRequestDto requestDto = new SkillsRequestDto();
        requestDto.setName("Spring Boot");

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(existingSkill));

        Message result = skillsService.updateSkill(requestDto, 1L);

        assertEquals("Skill updated successfully", result.getMessage());

        verify(skillRepository).findById(1L);

        verify(skillRepository).save(argThat(skill ->
                "Spring Boot".equals(skill.getName())
                        && skill.getId() == 1L
        ));
    }


    // 5. UPDATE SKILL - NOT FOUND

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingSkill() {

        SkillsRequestDto requestDto = new SkillsRequestDto();
        requestDto.setName("Spring Boot");

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        SkillNotFoundException exception = assertThrows(
                SkillNotFoundException.class,
                () -> skillsService.updateSkill(requestDto, 99L)
        );

        assertEquals("skill does not exists", exception.getMessage());

        verify(skillRepository, never()).save(any(Skills.class));
    }


    // 6. CONVERT ENTITY TO RESPONSE DTO

    @Test
    void shouldConvertSkillToResponseDto() {

        Skills skill = new Skills();
        skill.setId(1L);
        skill.setName("Java");

        SkillsResponseDto result =
                skillsService.getSkillResponseDto(skill);

        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());
    }


    // 7. FIND SKILL BY ID - SUCCESS

    @Test
    void shouldFindSkillById() {

        Skills skill = new Skills();
        skill.setId(1L);
        skill.setName("Java");

        when(skillRepository.findById(1L))
                .thenReturn(Optional.of(skill));

        SkillsResponseDto result = skillsService.findSkillById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());

        verify(skillRepository).findById(1L);
    }


    // 8. FIND SKILL BY ID - NOT FOUND

    @Test
    void shouldThrowExceptionWhenFindingNonExistingSkill() {

        when(skillRepository.findById(99L))
                .thenReturn(Optional.empty());

        SkillNotFoundException exception = assertThrows(
                SkillNotFoundException.class,
                () -> skillsService.findSkillById(99L)
        );

        assertEquals("skill does not exists", exception.getMessage());
    }


    // 9. FIND ALL SKILLS - SUCCESS

    @Test
    void shouldFindAllSkills() {

        Skills skill1 = new Skills();
        skill1.setId(1L);
        skill1.setName("Java");

        Skills skill2 = new Skills();
        skill2.setId(2L);
        skill2.setName("Spring Boot");

        when(skillRepository.findAll())
                .thenReturn(List.of(skill1, skill2));

        List<SkillsResponseDto> result = skillsService.findAllSkills();

        assertEquals(2, result.size());

        assertEquals("Java", result.get(0).getName());
        assertEquals("Spring Boot", result.get(1).getName());

        verify(skillRepository).findAll();
    }


    // 10. FIND ALL SKILLS - EMPTY LIST

    @Test
    void shouldReturnEmptyListWhenNoSkillsExist() {

        when(skillRepository.findAll())
                .thenReturn(List.of());

        List<SkillsResponseDto> result = skillsService.findAllSkills();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(skillRepository).findAll();
    }


    // 11. FIND BY SKILL NAME - SUCCESS

    @Test
    void shouldFindSkillsByName() {

        Skills skill1 = new Skills();
        skill1.setId(1L);
        skill1.setName("Java");

        Skills skill2 = new Skills();
        skill2.setId(2L);
        skill2.setName("Java");

        when(skillRepository.findSkillsByName("Java"))
                .thenReturn(List.of(skill1, skill2));

        List<SkillsResponseDto> result =
                skillsService.findBySkillName("Java");

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("Java", result.get(1).getName());

        verify(skillRepository).findSkillsByName("Java");
    }


    // 12. FIND BY SKILL NAME - NO MATCHES

    @Test
    void shouldReturnEmptyListWhenSkillNameDoesNotExist() {

        when(skillRepository.findSkillsByName("Python"))
                .thenReturn(List.of());

        List<SkillsResponseDto> result =
                skillsService.findBySkillName("Python");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(skillRepository).findSkillsByName("Python");
    }
}
