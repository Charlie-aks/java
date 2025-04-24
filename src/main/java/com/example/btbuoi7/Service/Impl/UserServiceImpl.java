package com.example.btbuoi7.Service.Impl;

import com.example.btbuoi7.DTO.UserLoginDTO;
import com.example.btbuoi7.DTO.UserRegisterDTO;
import com.example.btbuoi7.Entity.User;

import com.example.btbuoi7.Exception.CustomException;
import com.example.btbuoi7.Repository.UserRepository;
import com.example.btbuoi7.Security.JwtUtil;
import com.example.btbuoi7.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 1. Phương thức đăng ký
    @Override
    public void register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_" + dto.getRole().toUpperCase());
        userRepository.save(user);
    }

    // 2. Phương thức đăng nhập
    @Override
    public String login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return jwtUtil.generateToken(user);
    }

    // 3. Các phương thức OAuth2 mới
    @Override
    public void linkGoogleAccount(Long userId, String googleToken) {
        // Triển khai logic liên kết Google
    }

    @Override
    public void linkFacebookAccount(Long userId, String facebookToken) {
        // Triển khai logic liên kết Facebook
    }

    @Override
    public void unlinkGoogleAccount(Long userId) {
        // Triển khai logic hủy liên kết Google
    }

    @Override
    public void unlinkFacebookAccount(Long userId) {
        // Triển khai logic hủy liên kết Facebook
    }
}