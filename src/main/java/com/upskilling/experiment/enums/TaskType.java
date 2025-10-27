package com.upskilling.experiment.enums;

/**
 * Enum representing the type of work item.
 * Used for categorizing tasks by their nature (user story, bug, etc.).
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
public enum TaskType {
    /** User story representing a feature requirement */
    STORY,
    
    /** General task work item */
    TASK,
    
    /** Subtask of a larger task */
    SUBTASK,
    
    /** Bug or defect to be fixed */
    BUG
}
