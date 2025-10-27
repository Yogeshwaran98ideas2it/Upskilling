package com.upskilling.experiment.dto.response;

import lombok.Data;

/**
 * DTO for JWT response containing authentication token and user details.
 *
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Data
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String role;

    public JwtResponseDTO(String token, Long id, String username, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
    }
}