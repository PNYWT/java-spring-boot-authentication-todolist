package com.authentication.authentication.services;

import com.authentication.authentication.dto.LoginDto;
import com.authentication.authentication.dto.SignupDto;
import com.authentication.authentication.models.RoleModel;
import com.authentication.authentication.models.UserModel;
import com.authentication.authentication.repository.RoleRepository;
import com.authentication.authentication.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserModel registerUser(SignupDto signupDto) {

        if (userRepository.existsByUsername(signupDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        if (signupDto.getRoles() == null || signupDto.getRoles().isBlank()) {
            signupDto.setRoles("USER");
        }

        UserModel user = new UserModel();
        user.setName(signupDto.getName());
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        RoleModel role = roleRepository.findByName(signupDto.getRoles())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Collections.singleton(role));

        return userRepository.save(user);
    }

    public void authenticateUser(LoginDto loginDto) {
        // สร้าง Object UsernamePasswordAuthenticationToken ซึ่งเป็น Token ที่ Spring ใช้ตรวจสอบ
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword());
        // ใช้ AuthenticationManager (ซึ่ง Spring Security จัดการให้) ตรวจสอบว่าผู้ใช้ Login ถูกหรือไม่
        // ใช้สำหรับเก็บสถานะของผู้ใช้งานปัจจุบันใน
//        Authentication authentication = authenticationManager.authenticate(token);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

