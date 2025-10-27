package com.upskilling.experiment.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.Customizer;

import com.upskilling.experiment.enums.UserRole;

import com.upskilling.experiment.service.impl.CustomUserDetailsServiceImpl;


/**
 * Spring Security configuration for JWT-based authentication.
 * Configures security filter chain, authentication providers, and role-based access control.
 * Defines public endpoints (auth, API docs) and secured endpoints.
 * Implements stateless authentication with JWT tokens.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Custom user details service for authentication */
    private final CustomUserDetailsServiceImpl userDetailsService;
    
    /** JWT token filter for request authentication */
    private final AuthTokenFilter authTokenFilter;

    /**
     * Constructor for dependency injection
     * 
     * @param userDetailsService The user details service to be injected
     * @param authTokenFilter The JWT token filter to be injected
     */
    public SecurityConfig(CustomUserDetailsServiceImpl userDetailsService, AuthTokenFilter authTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .anonymous(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/api/auth/**",
                    "/api-docs",
                    "/api-docs/**",
                    "/docs",
                    "/docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                .requestMatchers("/api/projects/**").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MANAGER.name())
                .requestMatchers("/api/lists/**").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MANAGER.name())
                .requestMatchers("/api/tags/**").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MANAGER.name())
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            );

        http.authenticationProvider(authenticationProvider()); 
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}