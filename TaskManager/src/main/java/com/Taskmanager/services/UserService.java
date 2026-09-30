package com.Taskmanager.services;


import com.Taskmanager.entities.User;
import com.Taskmanager.exceptions.ResourceNotFoundException;
import com.Taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                         .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        CustomUserDetail customUserDetail = new CustomUserDetail(user);
        return customUserDetail;
    }
}
