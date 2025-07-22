package com.example.blogMs.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogMs.entities.User;
import com.example.blogMs.enums.Role;
import com.example.blogMs.payload.JwtResponse;
import com.example.blogMs.payload.LoginRequest;
import com.example.blogMs.payload.SignupRequest;
import com.example.blogMs.repositories.UserRepository;
import com.example.blogMs.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
          
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(loginRequest.getUsername());
        
        String role = authentication.getAuthorities().stream()
            .map(item -> item.getAuthority().toUpperCase())
            .collect(Collectors.joining(","));
        
        return new JwtResponse(jwt, loginRequest.getUsername(), role);
    }
    
    @PostMapping("/signup")
    public String registerUser(@RequestBody SignupRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return "Error: Username is already taken!";
        }
        
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        switch (signUpRequest.getRole().toUpperCase()) {
            case "ADMIN" -> user.setRole(Role.ADMIN);
            case "AUTHOR" -> user.setRole(Role.AUTHOR);
            default -> user.setRole(Role.READER);
        }

        userRepository.save(user);
        return "User registered successfully!";
    }
}
