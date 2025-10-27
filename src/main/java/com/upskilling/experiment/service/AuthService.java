package com.upskilling.experiment.service;

import org.springframework.http.ResponseEntity;

import com.upskilling.experiment.dto.request.RegisterRequestDTO;

/**
 * Service interface for authentication and user registration operations.
 * Handles user login with JWT token generation and new user registration.
 * Supports role-based authorization for elevated user creation.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface AuthService {

    /**
     * Authenticate a user and generate JWT token
     * 
     * @param username User's username
     * @param password User's password
     * @return ResponseEntity with JWT token and user details
     */
    ResponseEntity<?> authenticate(String username, String password);

    /**
     * Register a new user in the system
     * 
     * @param registerRequest DTO containing user registration details
     * @return ResponseEntity with success or error message
     */
    ResponseEntity<String> registerUser(RegisterRequestDTO registerRequest);
}
