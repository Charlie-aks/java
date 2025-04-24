package com.example.btbuoi7.Controller;

import com.example.btbuoi7.Entity.User;
import com.example.btbuoi7.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Yêu cầu quyền ADMIN cho tất cả các endpoint
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAdminData() {
        return ResponseEntity.ok("Hello admin!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{username}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userRepository.deleteByUsername(username);
        return ResponseEntity.ok("Đã xóa user: " + username);
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody User updatedUser) {

        User existingUser = userRepository.findByUsername(username)
                .orElse(null);
        if (existingUser == null) {
            return ResponseEntity.status(404).body("Không tìm thấy user: " + username);
        }

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        userRepository.save(existingUser);
        return ResponseEntity.ok("Đã cập nhật user: " + username);
    }
}