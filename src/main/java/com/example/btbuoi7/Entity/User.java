package com.example.btbuoi7.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username không được để trống")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password không được để trống")
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Role không được để trống")
    private String role; // ROLE_USER hoặc ROLE_ADMIN

    @Column(name = "google_id")
    private String googleId; // ID từ Google OAuth2

    @Column(name = "facebook_id")
    private String facebookId; // ID từ Facebook OAuth2

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
