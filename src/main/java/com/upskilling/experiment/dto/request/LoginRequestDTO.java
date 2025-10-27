package com.upskilling.experiment.dto.request;

import lombok.Data;

/**
 * DTO for login request containing username and password.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}