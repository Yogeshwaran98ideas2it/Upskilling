package com.upskilling.experiment.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upskilling.experiment.dto.response.UserResponseDTO;
import com.upskilling.experiment.mapper.UserMapper;
import com.upskilling.experiment.repository.UserRepository;

/**
 * REST Controller for user management operations.
 * Provides endpoints for retrieving user information.
 * Note: User creation is handled by the AuthController registration endpoint.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /** Repository for database operations on User entities */
    private final UserRepository userRepository;
    
    /** Mapper for converting between User entities and DTOs */
    private final UserMapper userMapper;

    /**
     * Retrieve all users in the system
     * Returns a list of all registered users with their basic information
     * 
     * @return ResponseEntity with list of all users (HTTP 200)
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userMapper.toDtoList(userRepository.findAll());
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieve a specific user by their unique identifier
     * Returns user details including username, email, and role
     * 
     * @param id The unique identifier of the user
     * @return ResponseEntity with user details (HTTP 200)
     * @throws EntityNotFoundException if user with given ID doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }
}
