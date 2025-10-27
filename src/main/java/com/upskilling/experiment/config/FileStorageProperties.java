package com.upskilling.experiment.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for file storage settings.
 * This class binds configuration values from application.yml under the 'file.upload' prefix.
 * Used to configure the directory where uploaded files will be stored.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileStorageProperties {
    
    /**
     * The directory path where uploaded files will be stored.
     * Default value is "uploads" (relative to application root).
     * 
     * Can be configured in application.yml as:
     * file:
     *   upload:
     *     upload-dir: /path/to/your/upload/directory
     * 
     * Examples:
     * - Relative path: "uploads" (stored in {project-root}/uploads)
     * - Absolute path: "/var/www/uploads"
     * - Docker path: "/app/uploads" (for containerized environments)
     */
    private String uploadDir = "uploads"; // Default directory

}