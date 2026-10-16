package com.zaid.recruitment.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CandidateNotFoundException.class)
    public ResponseEntity<ExceptionMessages> candidateExceptionHandler(CandidateNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessages(exception.getMessage()));

    }
    @ExceptionHandler(RecruiterNotFoundException.class)
    public ResponseEntity<ExceptionMessages> recruiterExceptionHandler(RecruiterNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessages(exception.getMessage()));
    }
    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ExceptionMessages> jobExceptionHandler(JobNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessages(exception.getMessage()));
    }
    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ExceptionMessages> applicationExceptionHandler(ApplicationNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessages(exception.getMessage()));
    }
    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<ExceptionMessages> skillExceptionHandler(SkillNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessages(exception.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String,String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put((error).getField(), error.getDefaultMessage());

        });
        return errorMap;
    }
}
