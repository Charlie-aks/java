package com.example.btbuoi7.Mapper;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    // Kiểm tra xem người dùng có vai trò phù hợp hay không
    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {

        for (ConfigAttribute attribute : configAttributes) {
            String requiredRole = attribute.getAttribute();

            boolean hasRole = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(requiredRole));

            if (!hasRole) {
                throw new AccessDeniedException("Access denied: Missing role " + requiredRole);
            }
        }
    }

    // Cho phép tất cả ConfigAttribute được kiểm tra
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    // Cho phép tất cả loại class được sử dụng cho quyền truy cập
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}