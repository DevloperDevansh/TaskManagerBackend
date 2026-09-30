package com.Taskmanager.config;

import com.Taskmanager.services.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //constructor injection
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //create a password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http
                // Disable CSRF because we are building a REST API
                // and will use JWT for authentication.
                .csrf(csrf -> csrf.disable())

            // Define which APIs are publicly accessible
            // and which APIs require authentication.
            .authorizeHttpRequests(auth -> auth
                // Allow all APIs inside /api/auth
                .requestMatchers("/api/auth/**")
                .permitAll()
                    .requestMatchers("/task/**").hasRole("USER")
                // Every other API requires the user
                // to be authenticated.
                // Example:
                // GET /api/tasks
                // POST /api/tasks
                .anyRequest()
                .authenticated()
             ).addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        // Return the configured SecurityFilterChain
        return http.build();
    }

}
