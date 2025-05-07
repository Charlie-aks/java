package com.example.btbuoi7.Controller;

import com.example.btbuoi7.DTO.UserDTO;
import com.example.btbuoi7.DTO.UserLoginDTO;
import com.example.btbuoi7.DTO.UserRegisterDTO;
import com.example.btbuoi7.Entity.User;
import com.example.btbuoi7.Security.TokenBlacklist;
import com.example.btbuoi7.Service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenBlacklist tokenBlacklist;
    
    // Rate limiting bucket
    private final Bucket bucket = Bucket4j.builder()
            .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
            .build();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO dto) {
        if (bucket.tryConsume(1)) {
            userService.register(dto);
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.status(429).body("Too many requests");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        if (bucket.tryConsume(1)) {
            String token = userService.login(dto);
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(429).body("Too many requests");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            tokenBlacklist.add(token);
        }
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getCurrentUser(user.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(
            @AuthenticationPrincipal User user,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(user.getId(), userDTO));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal User user,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(user.getId(), oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount(@AuthenticationPrincipal User user) {
        userService.deactivateAccount(user.getId());
        return ResponseEntity.ok("Account deactivated successfully");
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateAccount(@AuthenticationPrincipal User user) {
        userService.activateAccount(user.getId());
        return ResponseEntity.ok("Account activated successfully");
    }

    @PostMapping("/link/google")
    public ResponseEntity<?> linkGoogleAccount(
            @RequestParam String googleToken,
            @AuthenticationPrincipal User user) {
        userService.linkGoogleAccount(user.getId(), googleToken);
        return ResponseEntity.ok("Liên kết với Google thành công");
    }

    @PostMapping("/link/facebook")
    public ResponseEntity<?> linkFacebookAccount(
            @RequestParam String facebookToken,
            @AuthenticationPrincipal User user) {
        userService.linkFacebookAccount(user.getId(), facebookToken);
        return ResponseEntity.ok("Liên kết với Facebook thành công");
    }

    @PostMapping("/unlink/google")
    public ResponseEntity<?> unlinkGoogleAccount(@AuthenticationPrincipal User user) {
        userService.unlinkGoogleAccount(user.getId());
        return ResponseEntity.ok("Hủy liên kết với Google thành công");
    }

    @PostMapping("/unlink/facebook")
    public ResponseEntity<?> unlinkFacebookAccount(@AuthenticationPrincipal User user) {
        userService.unlinkFacebookAccount(user.getId());
        return ResponseEntity.ok("Hủy liên kết với Facebook thành công");
    }
}

