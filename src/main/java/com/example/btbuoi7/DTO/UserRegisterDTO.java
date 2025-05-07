package com.example.btbuoi7.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegisterDTO {
    
    @NotBlank(message = "Username không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username chỉ được chứa chữ cái và số, không dấu cách")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$", 
             message = "Password phải chứa ít nhất 1 số, 1 chữ hoa, 1 chữ thường và 1 ký tự đặc biệt")
    private String password;

    @NotBlank(message = "Role không được để trống")
    private String role;
}