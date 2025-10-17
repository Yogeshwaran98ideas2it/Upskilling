package com.upskilling.experiment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for TaaS Admin Service.
 * 
 * Configures Swagger/OpenAPI documentation for the admin service.
 * Uses dynamic server detection to work with any host/IP address.
 * 
 * @author TaaS Team
 * @version 1.0.0
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configure OpenAPI documentation.
     * 
     * Removes hardcoded server URLs to allow dynamic server detection.
     * This enables the application to work seamlessly in localhost, EC2,
     * or any other environment without configuration changes.
     * 
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Upskilling API 1.0.0")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TaaS Team")
                                .email("support@taas.com")
                                .url("https://taas.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                // Removed hardcoded servers list to enable dynamic server detection
                // This allows Swagger UI to automatically use the current request's host/port
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token for authentication. Include 'Bearer ' prefix followed by your JWT token.")));
    }
} 