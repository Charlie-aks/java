package com.example.btbuoi7.Repository;

import com.example.btbuoi7.Entity.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByFacebookId(String facebookId);
    
    @Transactional
    void deleteByUsername(String username);
}
 