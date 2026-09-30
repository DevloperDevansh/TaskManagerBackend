package com.Taskmanager.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    // Constructor Injection
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserService userService) {

        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check whether header contains Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            // No JWT token found
            // Continue request without authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Remove "Bearer " from the beginning
        String token = authHeader.substring(7);

        // Step 4: Extract email from JWT
        String email = jwtService.extractEmail(token);

        // Step 5: Check whether email exists
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6: Load user from database
            UserDetails userDetails =
                    userService.loadUserByUsername(email);

            // Step 7: Validate JWT
            if (jwtService.isTokenValid(token, userDetails)) {

                // Step 8: Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Step 9: Add request details
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Step 10: Tell Spring Security user is authenticated
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        // Step 11: Continue request
        filterChain.doFilter(request, response);
    }
}