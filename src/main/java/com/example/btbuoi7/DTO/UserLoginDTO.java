package com.example.btbuoi7.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;
}