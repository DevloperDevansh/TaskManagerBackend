package com.Taskmanager.controller;

import com.Taskmanager.dto.LoginRequest;
import com.Taskmanager.dto.LoginResponse;
import com.Taskmanager.dto.RegisterRequest;
import com.Taskmanager.dto.UserResponse;
import com.Taskmanager.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor Injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // CHECK API
    // =========================

    @GetMapping("/checkApi")
    public ResponseEntity<String> checkApi() {

        return new ResponseEntity<>(
                "Your API is running perfectly",
                HttpStatus.OK
        );
    }

    // =========================
    // REGISTER USER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> userRegister(
            @RequestBody RegisterRequest registerRequest) {

        UserResponse userResponse =
                authService.registerUser(registerRequest);

        return new ResponseEntity<>(
                userResponse,
                HttpStatus.CREATED
        );
    }

    // =========================
    // LOGIN USER
    // =========================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse =
                authService.loginUser(loginRequest);

        return new ResponseEntity<>(
                loginResponse,
                HttpStatus.OK
        );
    }
}