package com.example.SecuritySpring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SecuritySpring.Repository.AuditRepository;
import com.example.SecuritySpring.Repository.UserRepository;
import com.example.SecuritySpring.model.AuditLog;
import com.example.SecuritySpring.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditRepository auditRepository;

    int MAX_FAILED_ATTEMPTS = 3; 

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        
        org.springframework.security.core.userdetails.User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoles().stream().map(r -> r.getName()).toArray(String[]::new));

        return builder.build();
    }

   
    public void increaseFailedAttempts(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            int newFail = user.getFailedAttempt() + 1;
            user.setFailedAttempt(newFail);
            String action = "LOGIN_FAILED";

            if (newFail >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLocked(true);
                action = "ACCOUNT_LOCKED";
            }

            userRepository.save(user);
            auditRepository.save(new AuditLog(username, action)); 
        });
    }

    
    public void resetFailedAttempts(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFailedAttempt(0);
            userRepository.save(user);
            auditRepository.save(new AuditLog(username, "LOGIN_SUCCESS")); 
        });
    }
}