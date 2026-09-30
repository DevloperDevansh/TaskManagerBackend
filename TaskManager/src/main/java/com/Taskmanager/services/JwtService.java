package com.Taskmanager.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    /*
     * Secret key used to sign JWT.
     *
     * IMPORTANT:
     * Keep this secret in application.properties
     * in a real project.
     */
    private final String SECRET_KEY =
            "my-super-secret-key-for-task-manager-123456789";

    /*
     * Create signing key from our secret string.
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    /*
     * Generate JWT token.
     */
    public String generateToken(String email) {
        Date currentTime = new Date();
        // Token expires after 1 hour
        Date expirationTime =
                new Date(currentTime.getTime() + 60 * 60 * 1000);
        return Jwts.builder()
                // Store email inside JWT
                .setSubject(email)
                // Token creation time
                .setIssuedAt(currentTime)
                // Token expiry time
                .setExpiration(expirationTime)
                // Sign JWT
                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )
                // Convert JWT to String
                .compact();
    }

    /*
     * Extract email from JWT.
     */
    public String extractEmail(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /*
     * Check whether JWT is valid.
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String email = extractEmail(token);

        return email.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /*
     * Check whether JWT has expired.
     */
    private boolean isTokenExpired(String token) {

        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}