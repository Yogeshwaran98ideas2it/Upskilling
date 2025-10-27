package com.upskilling.experiment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.request.LoginRequestDTO;
import com.upskilling.experiment.dto.request.RegisterRequestDTO;
import com.upskilling.experiment.service.AuthService;

/**
 * REST Controller for authentication operations including login and user registration.
 * Handles user authentication with JWT token generation and new user registration.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Service for handling authentication business logic */
    private final AuthService authService;

    /**
     * Constructor for dependency injection
     * 
     * @param authService The authentication service to be injected
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticate a user with username and password
     * Generates and returns a JWT token upon successful authentication
     * 
     * @param loginRequest DTO containing username and password credentials
     * @return ResponseEntity containing JWT token and user information (id, username, role)
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        // Delegate authentication logic to service layer
        return authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
    }
    
    /**
     * Register a new user in the system
     * Validates user information and creates a new user account
     * 
     * @param registerRequest DTO containing username, email, and password for new user
     * @return ResponseEntity with success message
     * @throws jakarta.validation.ConstraintViolationException if validation fails
     * @throws org.springframework.dao.DataIntegrityViolationException if username or email already exists
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        // Delegate user registration logic to service layer
        return authService.registerUser(registerRequest);
    }
}