package com.zaid.recruitment.Auth;

import com.zaid.recruitment.Service.Message;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/addCandidate")
    public ResponseEntity<Message> addUserCandidate( @RequestBody @Valid UserRequestDtoCandidate userRequestDtoCandidate){
        return new ResponseEntity<>(userService.addUserAsCandidate(userRequestDtoCandidate), HttpStatus.CREATED);
    }
    @PostMapping("/addRecruiter")
    public ResponseEntity<Message> addUserRecruiter(@RequestBody @Valid UserRequestDtoRecruiter userRequestDtoRecruiter){
        return new ResponseEntity<>(userService.addUserAsRecruiter(userRequestDtoRecruiter), HttpStatus.CREATED);
    }
}
