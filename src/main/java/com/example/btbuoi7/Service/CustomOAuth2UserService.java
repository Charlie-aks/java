package com.example.btbuoi7.Service;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.btbuoi7.Entity.User;
import com.example.btbuoi7.Repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = getEmail(provider, attributes);
        String name = getName(provider, attributes);
        String providerId = getProviderId(provider, attributes);

        // Kiểm tra xem user đã tồn tại chưa
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Nếu không tồn tại, tạo user mới
                    User newUser = new User();
                    newUser.setUsername(name);
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    newUser.setRole("ROLE_USER");
                    return newUser;
                });

        // Cập nhật provider ID
        if ("google".equals(provider)) {
            user.setGoogleId(providerId);
        } else if ("facebook".equals(provider)) {
            user.setFacebookId(providerId);
        }

        userRepository.save(user);

        return oAuth2User;
    }

    private String getEmail(String provider, Map<String, Object> attributes) {
        if ("google".equals(provider)) {
            return (String) attributes.get("email");
        } else if ("facebook".equals(provider)) {
            return (String) attributes.get("email");
        }
        throw new OAuth2AuthenticationException("Provider không được hỗ trợ");
    }

    private String getName(String provider, Map<String, Object> attributes) {
        if ("google".equals(provider)) {
            return (String) attributes.get("name");
        } else if ("facebook".equals(provider)) {
            return (String) attributes.get("name");
        }
        throw new OAuth2AuthenticationException("Provider không được hỗ trợ");
    }

    private String getProviderId(String provider, Map<String, Object> attributes) {
        if ("google".equals(provider)) {
            return (String) attributes.get("sub");
        } else if ("facebook".equals(provider)) {
            return (String) attributes.get("id");
        }
        throw new OAuth2AuthenticationException("Provider không được hỗ trợ");
    }
}
