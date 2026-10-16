package com.zaid.recruitment.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenGeneratingUrl {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService ;
    @GetMapping("/generateToken")
    public String tokenGeneratingUrl(@RequestParam String name, @RequestParam String password) {
        // String userName = name;
        //  String password1 = password;
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(name, password);
        //user.setDetails(student);
        System.out.println("tokenGeneratingUrl");
        Authentication authentication = authenticationManager.authenticate(user);
        if (authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            return jwtService.generateToken(name, role);
        }
        return null;
    }


}