package com.upskilling.experiment.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT token configuration and utility class.
 * Handles JWT token generation, validation, and claims extraction.
 * Uses HMAC SHA-512 algorithm for signing tokens.
 * Configurable secret key and expiration time via application properties.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Component
public class JwtConfig {

    /** JWT secret key for signing tokens - loaded from application.properties */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /** JWT token expiration time in milliseconds - loaded from application.properties */
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generate a signing key from the JWT secret
     * Uses SHA-512 to derive a key suitable for HMAC algorithms
     * 
     * @return Key for signing JWT tokens
     * @throws IllegalStateException if key generation fails
     */
    private Key getSigningKey() {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            byte[] keyBytes = md.digest(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to derive signing key", e);
        }
    }
    
    /**
     * Generate a JWT token from Spring Security Authentication object
     * Includes user's role as a claim in the token
     * 
     * @param authentication The authentication object containing user details
     * @return Signed JWT token string
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        // Extract role from authorities and add as claim
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userPrincipal.getAuthorities().iterator().next().getAuthority());

        // Build and sign the JWT token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Validate a JWT token against user details
     * Checks if username matches and token is not expired
     * 
     * @param token The JWT token to validate
     * @param userDetails The user details to validate against
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extract all claims from a JWT token
     * 
     * @param token The JWT token to parse
     * @return Claims object containing all token claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Extract a specific claim from a JWT token
     * Generic method to extract any claim type
     * 
     * @param token The JWT token
     * @param claimsResolver Function to extract specific claim
     * @return The extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract username from JWT token
     * Username is stored as the token subject
     * 
     * @param token The JWT token
     * @return Username extracted from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from JWT token
     * 
     * @param token The JWT token
     * @return Expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Check if JWT token is expired
     * Compares token expiration date with current date
     * 
     * @param token The JWT token to check
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}