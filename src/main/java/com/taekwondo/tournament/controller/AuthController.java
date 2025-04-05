package com.taekwondo.tournament.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taekwondo.tournament.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        logger.info("Login attempt for username: {}", credentials.get("username"));
        
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            logger.warn("Login failed: Missing username or password");
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required"));
        }

        return userService.findByUsername(username)
            .map(user -> {
                logger.info("User found: {}", user.getUsername());
                if (passwordEncoder.matches(password, user.getPassword())) {
                    logger.info("Password matched for user: {}", username);
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", "dummy-token-" + System.currentTimeMillis());
                    response.put("user", user);
                    return ResponseEntity.ok(response);
                } else {
                    logger.warn("Password mismatch for user: {}", username);
                    return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
                }
            })
            .orElseGet(() -> {
                logger.warn("User not found: {}", username);
                return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
            });
    }
} 