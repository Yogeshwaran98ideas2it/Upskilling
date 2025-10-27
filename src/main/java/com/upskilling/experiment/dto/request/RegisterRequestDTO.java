package com.upskilling.experiment.dto.request;


import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.upskilling.experiment.enums.UserRole;

/**
 * DTO for user registration.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class RegisterRequestDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    
    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Must be a valid email format")
    private String email;

    private UserRole role;

}