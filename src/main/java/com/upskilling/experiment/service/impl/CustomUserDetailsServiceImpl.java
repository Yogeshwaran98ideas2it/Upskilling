package com.upskilling.experiment.service.impl;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.repository.UserRepository;


/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Loads user-specific data for authentication and authorization purposes.
 * Integrates with the application's User repository to provide user details.
 * Converts User entity to Spring Security's UserDetails object with role-based authorities.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    /** Repository for database operations on User entities */
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection
     * 
     * @param userRepository The user repository to be injected
     */
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user details by username for authentication
     * Retrieves user from database and creates Spring Security UserDetails object
     * with username, password, and role-based authorities
     * 
     * @param username The username to load
     * @return UserDetails object for Spring Security authentication
     * @throws UsernameNotFoundException if user with given username doesn't exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}