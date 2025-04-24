package com.example.btbuoi7.Security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {

    private final Set<String> blacklist = new HashSet<>();

    // ✅ Thêm token vào danh sách đen
    public void add(String token) {
        blacklist.add(token);
    }

    // ✅ Kiểm tra xem token có bị vô hiệu hóa không
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
