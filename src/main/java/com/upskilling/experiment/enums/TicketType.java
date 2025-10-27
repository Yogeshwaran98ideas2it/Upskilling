package com.upskilling.experiment.enums;

/**
 * Enum representing the backlog type for task organization.
 * Used to categorize tasks into different work queues.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public enum TicketType {
    /** Task in the backlog (not yet in active work) */
    BACKLOG,
    
    /** Task in current sprint (active work) */
    SPRINT
}
