package com.authentication.authentication.services;

import com.authentication.authentication.models.UserModel;
import com.authentication.authentication.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /// ใช้ตอน Login
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // ค้นหาผู้ใช้งานจาก database ด้วย username หรือ email (ถ้าตรงอันใดอันหนึ่งก็ใช้ได้)
        UserModel user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
        // ถ้าไม่เจอผู้ใช้ จะโยน Exception และหยุดการทำงานทันที

        // สร้างสิทธิ์ (Authority) สำหรับผู้ใช้งาน จาก roles ที่ผู้ใช้มี
        Set<GrantedAuthority> authorities = user
                // ดึง roles ของ user เช่น ADMIN, USER
                .getRoles()
                .stream()
                // แปลง RoleModel → GrantedAuthority
                .map(roleModel -> new SimpleGrantedAuthority(roleModel.getName()))
                // รวมเป็น Set<GrantedAuthority>
                .collect(Collectors.toSet());

        // สร้างและคืนค่า UserDetails ให้ Spring Security ใช้ในการยืนยันตัวตน
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // ใช้ email เป็น username (สามารถเปลี่ยนเป็น user.getUsername() ได้ถ้าต้องการ)
                user.getPassword(), // password ที่ถูกเข้ารหัสไว้ (เช่น bcrypt)
                authorities  // สิทธิ์ของผู้ใช้งาน
        );
    }
}
