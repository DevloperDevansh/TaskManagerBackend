package com.Taskmanager.services;

import com.Taskmanager.dto.LoginRequest;
import com.Taskmanager.dto.LoginResponse;
import com.Taskmanager.dto.RegisterRequest;
import com.Taskmanager.dto.UserResponse;
import com.Taskmanager.entities.Role;
import com.Taskmanager.entities.User;
import com.Taskmanager.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    //constructor injection
    public AuthService(UserRepository userRepository , PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    //Register user
    public UserResponse registerUser(RegisterRequest request){
        //check if email is already exist
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email Already exists.....");
        }
        //create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        //set role
        user.setRole(Role.USER);
        //set bcyrpt pass
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //saved user
        User savedUser = userRepository.save(user);
        //save user
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    //create login method
    public LoginResponse loginUser(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        // Step 2: Generate JWT after successful authentication
        String token = jwtService.generateToken(
                loginRequest.getEmail()
        );

        // Step 3: Return JWT token
        return new LoginResponse(token);
    }
}
