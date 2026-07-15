package com.istore.api.auth;

import com.istore.api.user.Role;
import com.istore.api.user.User;
import com.istore.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        // 1. Email එක දැනටමත් තියෙනවද?
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // 2. User ව හදනවා — password එක BCrypt hash කරලා!
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .nic(request.getNic())
                .role(Role.CUSTOMER)          // register වෙන හැමෝම CUSTOMER
                .suspended(false)
                .createdAt(Instant.now())
                .build();

        // 3. MongoDB එකට save!
        user = userRepository.save(user);

        // 4. Token එකක් හදලා response එක යවනවා
        return buildResponse(user);
    }

    public AuthResponse login(LoginRequest request) {

        // 1. Email එකෙන් user ව හොයනවා
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // 2. Password එක match ද? (hash එකත් එක්ක compare)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // 3. Suspended ද?
        if (user.isSuspended()) {
            throw new IllegalArgumentException("Account is suspended");
        }

        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}