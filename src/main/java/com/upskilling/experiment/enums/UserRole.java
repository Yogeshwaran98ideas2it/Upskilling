package com.upskilling.experiment.enums;

/**
 * Enum representing user roles in the system for access control.
 * Defines what permissions each user type has within the application.
 * Used for role-based security and authorization.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public enum UserRole {
    /** Administrator role - full system access, can manage users and all resources */
    ADMIN,
    
    /** Manager role - can manage projects and tasks, limited user management */
    MANAGER,
    
    /** Member role - basic access, can view and work on assigned tasks */
    MEMBER
}
