package com.authentication.authentication.dto;

import com.authentication.authentication.models.RoleModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class SignupDto {
    @NotBlank(message = "Name is require.")
    private String name;

    @NotBlank(message = "Username is require.")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is require.")
    private String email;

    @NotBlank(message = "Password Email is require.")
    private String password;

    private String roles;
}
