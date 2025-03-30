package com.taekwondo.tournament.controller;

import com.taekwondo.tournament.model.User;
import com.taekwondo.tournament.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            logger.info("Login attempt for username: {}", loginRequest.get("username"));
            
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            if (username == null || password == null) {
                logger.warn("Login failed: username or password is null");
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "Username and password are required"
                ));
            }

            return userService.findByUsername(username)
                .map(user -> {
                    logger.info("Found user: {}", user.getUsername());
                    logger.info("Stored password hash: {}", user.getPassword());
                    logger.info("Attempting to match password: {}", password);
                    
                    boolean matches = passwordEncoder.matches(password, user.getPassword());
                    logger.info("Password match result: {}", matches);
                    
                    if (!matches) {
                        logger.warn("Login failed: password does not match for user: {}", username);
                        return ResponseEntity.badRequest().body(Map.of(
                            "message", "Invalid username or password"
                        ));
                    }
                    
                    logger.info("Login successful for user: {}", username);
                    user.setPassword(null);
                    return ResponseEntity.ok(Map.of(
                        "user", user,
                        "token", "dummy-token-for-now"
                    ));
                })
                .orElseGet(() -> {
                    logger.warn("Login failed: user not found: {}", username);
                    return ResponseEntity.badRequest().body(Map.of(
                        "message", "Invalid username or password"
                    ));
                });
        } catch (Exception e) {
            logger.error("Login error", e);
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Login failed: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            logger.info("Registration attempt for username: {}", user.getUsername());
            
            if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getRole() == null) {
                logger.warn("Registration failed: missing required fields");
                return ResponseEntity.badRequest().body(Map.of(
                    "message", "All fields are required"
                ));
            }
            
            User createdUser = userService.createUser(user);
            logger.info("Registration successful for user: {}", user.getUsername());
            
            createdUser.setPassword(null);
            return ResponseEntity.ok(Map.of(
                "user", createdUser,
                "token", "dummy-token-for-now"
            ));
        } catch (RuntimeException e) {
            logger.error("Registration error", e);
            return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            logger.info("Token validation attempt");
            String token = authHeader.replace("Bearer ", "");
            if ("dummy-token-for-now".equals(token)) {
                logger.info("Token validation successful");
                return ResponseEntity.ok(Map.of(
                    "message", "Token is valid"
                ));
            }
            logger.warn("Token validation failed: invalid token");
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Invalid token"
            ));
        } catch (Exception e) {
            logger.error("Token validation error", e);
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Token validation failed: " + e.getMessage()
            ));
        }
    }
} 