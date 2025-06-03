package com.authentication.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank(message = "Username or Email is require.")
    private String usernameOrEmail;
    @NotBlank(message = "Password is require.")
    private String password;
}
