package com.example.btbuoi7.Service;

import com.example.btbuoi7.DTO.UserLoginDTO;
import com.example.btbuoi7.DTO.UserRegisterDTO;

public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);
    String login(UserLoginDTO userLoginDTO);
    
    void linkGoogleAccount(Long userId, String googleToken);
    void linkFacebookAccount(Long userId, String facebookToken);
    void unlinkGoogleAccount(Long userId);
    void unlinkFacebookAccount(Long userId);
}