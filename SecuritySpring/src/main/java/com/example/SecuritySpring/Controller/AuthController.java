package com.example.SecuritySpring.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.SecuritySpring.Repository.RoleRepository;
import com.example.SecuritySpring.Repository.UserRepository;
import com.example.SecuritySpring.dto.RegisterRequest;
import com.example.SecuritySpring.model.Role;
import com.example.SecuritySpring.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    
    @PostMapping("/login-success")
    public ResponseEntity<Map<String, String>> loginSuccess(
            @RequestParam String username,
            @RequestParam String password) {

        var userOpt = userRepository.findByUsername(username);
        Map<String, String> response = new HashMap<>();

        if (userOpt.isEmpty()) {
            response.put("error", "Invalid username");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("error", "Invalid password");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("message", "Login successful");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/logout-success")
    public ResponseEntity<Map<String, String>> logoutSuccess(
            HttpServletRequest request,
            @RequestParam(required = false) String username) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        response.put("username", (username != null) ? username : "Unknown");
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, String>> dashboard(@RequestParam String username) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to dashboard");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(request.getRole());
        if (role == null) {
            response.put("error", "Invalid role");
            return ResponseEntity.badRequest().body(response);
        }

        user.setRoles(Set.of(role));
        userRepository.save(user);

        response.put("message", "registered successfully");
        response.put("role", request.getRole());
        return ResponseEntity.ok(response);
    }
}