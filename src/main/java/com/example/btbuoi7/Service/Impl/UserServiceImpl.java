package com.example.btbuoi7.Service.Impl;

import com.example.btbuoi7.DTO.UserDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new CustomException("Username đã tồn tại");
        }
        
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Email đã tồn tại");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_" + dto.getRole().toUpperCase());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }

    @Override
    @Transactional
    public String login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        if (!user.isActive()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return jwtUtil.generateToken(user);
    }

    @Override
    public UserDTO getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new CustomException("Email đã tồn tại");
            }
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                throw new CustomException("Username đã tồn tại");
            }
            user.setUsername(userDTO.getUsername());
        }

        userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại"));

        String resetToken = UUID.randomUUID().toString();
        // TODO: Lưu reset token vào database và gửi email
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // TODO: Kiểm tra token và cập nhật mật khẩu
    }

    @Override
    @Transactional
    public void linkGoogleAccount(Long userId, String googleToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // TODO: Xác thực googleToken và lấy googleId
        String googleId = "google_id_from_token";
        user.setGoogleId(googleId);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void linkFacebookAccount(Long userId, String facebookToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // TODO: Xác thực facebookToken và lấy facebookId
        String facebookId = "facebook_id_from_token";
        user.setFacebookId(facebookId);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlinkGoogleAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setGoogleId(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlinkFacebookAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setFacebookId(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void activateAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setActive(true);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setHasGoogleAccount(user.getGoogleId() != null);
        dto.setHasFacebookAccount(user.getFacebookId() != null);
        return dto;
    }
}