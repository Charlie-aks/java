package com.example.btbuoi7.Security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Cấu hình
    private final String jwtSecret = "thisIsASecretKeyForJwtTokenGenerationExample123456"; // > 32 bytes
    private final long jwtExpirationMs = 86400000; // 1 ngày

    private Key key;

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    // Tạo JWT
    public String generateToken(com.example.btbuoi7.Entity.User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Lấy thông tin username từ token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Lấy role từ token
    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Xác thực token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
 