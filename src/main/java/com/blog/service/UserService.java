package com.blog.service;

import com.blog.dto.request.LoginRequest;
import com.blog.dto.request.RegisterRequest;
import com.blog.dto.response.LoginResponse;
import com.blog.dto.response.UserResponse;
import com.blog.exception.DuplicateResourceException;
import com.blog.model.User;
import com.blog.model.enums.Role;
import com.blog.repository.UserRepository;
import com.blog.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setRole(Role.USER);
        User save = userRepository.save(user);
        return UserResponse.builder()
                .id(save.getId())
                .username(save.getUsername())
                .email(save.getEmail())
                .age(save.getAge())
                .role(save.getRole().toString())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtService.generateToken(user.getUsername());
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
