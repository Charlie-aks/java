package com.example.btbuoi7.Controller;

import com.example.btbuoi7.DTO.UserLoginDTO;
import com.example.btbuoi7.DTO.UserRegisterDTO;
import com.example.btbuoi7.Entity.User;
import com.example.btbuoi7.Security.TokenBlacklist;
import com.example.btbuoi7.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenBlacklist tokenBlacklist;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return ResponseEntity.ok("User registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            tokenBlacklist.add(token);
            System.out.println("Token added to blacklist: " + token); // Debug log
        }
        return ResponseEntity.ok("Logged out successfully.");
    }
    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(HttpServletRequest request) {
        // Xử lý sau khi đăng nhập OAuth2 thành công
        // Trả về JWT token hoặc thông tin user
        return ResponseEntity.ok("Đăng nhập OAuth2 thành công");
    }
 
    @PostMapping("/link/google")
    public ResponseEntity<?> linkGoogleAccount(@RequestParam String googleToken, 
                                             @AuthenticationPrincipal User user) {
        userService.linkGoogleAccount(user.getId(), googleToken);
        return ResponseEntity.ok("Liên kết với Google thành công");
    }

    @PostMapping("/link/facebook")
    public ResponseEntity<?> linkFacebookAccount(@RequestParam String facebookToken,
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

