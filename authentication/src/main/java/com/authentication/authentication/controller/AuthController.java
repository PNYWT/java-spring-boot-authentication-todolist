package com.authentication.authentication.controller;

import com.authentication.authentication.dto.BaseResponseDto;
import com.authentication.authentication.dto.LoginDto;
import com.authentication.authentication.dto.SignupDto;
import com.authentication.authentication.models.UserModel;
import com.authentication.authentication.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService signupService) {
        this.authService = signupService;
    }

    /// Register
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signupDto) {
        /*
        try {
            UserModel user = authService.registerUser(signupDto);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
         */
        try {
            UserModel user = authService.registerUser(signupDto);
            BaseResponseDto<UserModel> response = new BaseResponseDto<UserModel>()
                    .setBaseReposeData("101", "Sighup Success", user);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new BaseResponseDto<UserModel>()
                            .setBaseResponse("102", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponseDto<UserModel>()
                            .setBaseResponse("102", "Unexpected error occurred")
            );
        }
    }

    /// Login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        /*
          try {
            authService.authenticateUser(loginDto);
            BaseResponseDto reponse = new BaseResponseDto().setBaseResponse("101", "Login success");
            return ResponseEntity.ok(reponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
         */
        try {
            authService.authenticateUser(loginDto);
            BaseResponseDto reponse = new BaseResponseDto().setBaseResponse("101", "Login success");
            return ResponseEntity.ok(reponse);
        } catch (AuthenticationException e) {
            BaseResponseDto response = new BaseResponseDto().setBaseResponse("102", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
