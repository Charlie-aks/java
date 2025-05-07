package com.example.btbuoi7.Service;

import com.example.btbuoi7.DTO.UserDTO;
import com.example.btbuoi7.DTO.UserLoginDTO;
import com.example.btbuoi7.DTO.UserRegisterDTO;

public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);
    String login(UserLoginDTO userLoginDTO);
    
    UserDTO getCurrentUser(Long userId);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
    
    void linkGoogleAccount(Long userId, String googleToken);
    void linkFacebookAccount(Long userId, String facebookToken);
    void unlinkGoogleAccount(Long userId);
    void unlinkFacebookAccount(Long userId);
    
    void deactivateAccount(Long userId);
    void activateAccount(Long userId);
}