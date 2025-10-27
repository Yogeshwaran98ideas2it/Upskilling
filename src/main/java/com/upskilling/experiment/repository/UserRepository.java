package com.upskilling.experiment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upskilling.experiment.entity.User;

/**
 * Repository interface for database operations on User entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom query methods for username and role lookup.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User,Long> {
    /**
     * Find user by username (case-sensitive)
     * Used for authentication and user lookup
     * 
     * @param username The username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find users by role
     * Used for role-based queries and authorization checks
     * 
     * @param role The role to search for
     * @return Optional containing the first user with that role if found
     */
    Optional<User> findByRole(String role);
}
