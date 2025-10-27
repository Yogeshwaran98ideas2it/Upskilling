package com.upskilling.experiment.enums;

/**
 * Enum representing the priority level of a task.
 * Used for task prioritization and workflow management.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public enum TaskPriority {
    /** Low priority - can be completed when time permits */
    LOW,
    
    /** Medium priority - normal workload */
    MEDIUM,
    
    /** High priority - should be completed soon */
    HIGH,
    
    /** Critical priority - requires immediate attention */
    CRITICAL
}
