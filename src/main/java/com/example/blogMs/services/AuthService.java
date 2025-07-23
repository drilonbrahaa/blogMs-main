package com.example.blogMs.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blogMs.entities.User;
import com.example.blogMs.enums.Role;
import com.example.blogMs.payload.JwtResponse;
import com.example.blogMs.payload.LoginRequest;
import com.example.blogMs.payload.SignupRequest;
import com.example.blogMs.repositories.UserRepository;
import com.example.blogMs.security.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtil jwtUtil;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
          
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(loginRequest.getUsername());
        
        String role = authentication.getAuthorities().stream()
            .map(item -> item.getAuthority().toUpperCase())
            .collect(Collectors.joining(","));
        
        return new JwtResponse(jwt, loginRequest.getUsername(), role);
    }

    public boolean registerUser(SignupRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
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
        return true;
    }

}
