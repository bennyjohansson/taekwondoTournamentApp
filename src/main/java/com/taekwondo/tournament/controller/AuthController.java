package com.taekwondo.tournament.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taekwondo.tournament.model.User;
import com.taekwondo.tournament.security.JwtTokenProvider;
import com.taekwondo.tournament.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.get("username"));
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (username == null || password == null) {
            logger.warn("Login attempt failed: Missing username or password");
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        try {
            logger.debug("Attempting authentication for user: {}", username);
            logger.debug("Creating authentication token with username: {}", username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            logger.debug("Authentication token created, attempting authentication");
            
            Authentication authentication = authenticationManager.authenticate(authToken);
            logger.debug("Authentication successful, getting user details");
            
            User user = userService.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found after successful authentication: {}", username);
                    return new RuntimeException("User not found after successful authentication");
                });
            
            logger.debug("Generating JWT token for user: {}", username);
            String token = tokenProvider.generateToken(authentication);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            
            logger.info("Login successful for user: {}, role: {}", username, user.getRole());
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}. Error: {}", username, e.getMessage());
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            logger.error("Unexpected error during login for user: {}. Error: {}", username, e.getMessage(), e);
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }
    
    @PostMapping("/test-password")
    public ResponseEntity<?> testPassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        if (password == null) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        
        String encodedPassword = passwordEncoder.encode(password);
        logger.info("Password: {}, Encoded: {}", password, encodedPassword);
        
        // Get the admin user from the database
        User adminUser = userService.findByUsername("admin")
            .orElseThrow(() -> new RuntimeException("Admin user not found"));
        
        // Check if the password matches the hash in the database
        boolean matches = passwordEncoder.matches(password, adminUser.getPassword());
        logger.info("Password matches database hash: {}", matches);
        
        Map<String, Object> response = new HashMap<>();
        response.put("password", password);
        response.put("encoded", encodedPassword);
        response.put("databaseHash", adminUser.getPassword());
        response.put("matches", matches);
        
        return ResponseEntity.ok(response);
    }
} 