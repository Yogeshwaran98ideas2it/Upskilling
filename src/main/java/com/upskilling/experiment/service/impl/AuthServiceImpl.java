package com.upskilling.experiment.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.upskilling.experiment.config.JwtConfig;
import com.upskilling.experiment.dto.response.JwtResponseDTO;
import com.upskilling.experiment.dto.request.RegisterRequestDTO;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.enums.UserRole;
import com.upskilling.experiment.repository.UserRepository;
import com.upskilling.experiment.service.AuthService;

/**
 * Service implementation for authentication and user registration.
 * Handles JWT token generation, user login, and user registration with role-based authorization.
 * Integrates with Spring Security for authentication and password encoding.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    /** Repository for database operations on User entities */
    private final UserRepository userRepository;
    
    /** Password encoder for hashing passwords using BCrypt */
    private final PasswordEncoder passwordEncoder;
    
    /** Authentication manager for Spring Security authentication */
    private final AuthenticationManager authenticationManager;
    
    /** JWT configuration for token generation and validation */
    private final JwtConfig jwtConfig;

    /**
     * Constructor for dependency injection
     * 
     * @param authenticationManager The authentication manager to be injected
     * @param jwtConfig The JWT configuration to be injected
     * @param userRepository The user repository to be injected
     * @param passwordEncoder The password encoder to be injected
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtConfig jwtConfig, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticate a user and generate JWT token
     * Validates credentials, sets authentication context, and returns JWT token with user details
     * 
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return ResponseEntity with JWT token, user ID, username, and role
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are invalid
     */
    @Override
    public ResponseEntity<?> authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtConfig.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(new JwtResponseDTO(jwt, user.getId(), user.getUsername(), role));
    }

    /**
     * Register a new user in the system
     * Validates username uniqueness, handles role-based authorization for elevated roles (ADMIN/MANAGER),
     * and creates the user with encoded password
     * 
     * @param registerRequest DTO containing username, email, password, and optional role
     * @return ResponseEntity with success message or error details
     * @throws org.springframework.dao.DataIntegrityViolationException if username already exists
     * @throws org.springframework.security.access.AccessDeniedException if trying to register elevated role without authorization
     */
    @Override
    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequest) {

        // --- 1. Check for existing username ---
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // --- 2. Determine the requested role and perform Authorization Check ---

        // Get the role string from the DTO
        String requestedRoleString = registerRequest.getRole().toString();
        UserRole requestedRole = UserRole.MEMBER; // Default role

        // Check if a role string was provided and is not null or blank
        if (requestedRoleString != null && !requestedRoleString.trim().isEmpty()) {
            try {
                // Convert the provided string to the UserRole enum, assuming standard naming
                requestedRole = UserRole.valueOf(requestedRoleString.toUpperCase());
            } catch (IllegalArgumentException e) {
                // The provided role string is invalid (e.g., a role that doesn't exist in the enum)
                return new ResponseEntity<>("Invalid user role specified: " + requestedRoleString, HttpStatus.BAD_REQUEST);
            }
        }

        if (requestedRole != UserRole.MEMBER) {
            // This is an elevated registration request (e.g., ADMIN or MANAGER)

            try {
                // Get the authenticated principal (current user) from Spring Security Context
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

                // Fetch the full User entity from the database to check the role
                User mainUser = userRepository.findByUsername(userDetails.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database."));

                // Check authorization: must be ADMIN OR MANAGER
                boolean isAuthorized = mainUser.getRole().equals(UserRole.ADMIN) || mainUser.getRole().equals(UserRole.MANAGER);

                if (!isAuthorized) {
                    // Using 403 Forbidden as it correctly identifies an authorization failure
                    return new ResponseEntity<>("Current user is not Authorized to create users with elevated roles!", HttpStatus.FORBIDDEN);
                }
            } catch (UsernameNotFoundException e) {
                // If the user fetching fails (shouldn't happen with proper setup)
                return new ResponseEntity<>("Authorization principal could not be verified.", HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {
                // Catching exceptions like NullPointerException if no user is authenticated
                return new ResponseEntity<>("Authentication required for elevated user creation.", HttpStatus.UNAUTHORIZED);
            }
        }

        // --- 3. Create and Save the User ---
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Use the determined role
        user.setRole(requestedRole);

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
