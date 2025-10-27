package com.upskilling.experiment.enums;

/**
 * Enum representing the current status of a task in the workflow.
 * Used for tracking task progress through different stages.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public enum TaskStatus {
    /** Task has been created but not started yet */
    TO_DO,
    
    /** Task is currently being worked on */
    IN_PROGRESS,
    
    /** Task is being reviewed before completion */
    REVIEW,
    
    /** Task has been completed successfully */
    DONE,
    
    /** Task is blocked and cannot proceed */
    BLOCKED
}