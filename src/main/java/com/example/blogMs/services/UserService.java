package com.example.blogMs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blogMs.entities.User;
import com.example.blogMs.enums.Role;
import com.example.blogMs.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Error: Username cannot be empty!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!"); 
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Error: Password cannot be empty!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.READER);
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            
            if (userDetails.getUsername() != null) {
                if (userRepository.existsByUsername(userDetails.getUsername()) && !existingUser.getUsername().equals(userDetails.getUsername())) {
                    throw new RuntimeException("Error: Username is already taken!");
                } else if (userDetails.getUsername().trim().isEmpty()) {
                    throw new RuntimeException("Error: Username cannot be empty!");
                }
                existingUser.setUsername(userDetails.getUsername());
            }
            if (userDetails.getPassword() != null) {
                if (userDetails.getPassword().trim().isEmpty()) {
                    throw new RuntimeException("Error: Password cannot be empty!");
                }
                existingUser.setPassword(encoder.encode(userDetails.getPassword()));
            }
            if (userDetails.getRole() != null) {
                existingUser.setRole(userDetails.getRole());
            }
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
       return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole().name()))
);
    }
}
