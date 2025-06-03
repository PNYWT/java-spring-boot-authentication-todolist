package com.authentication.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customEntryPoint;

    public SecurityConfig(UserDetailsService userDetailsService,
                          CustomAuthenticationEntryPoint customEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.customEntryPoint = customEntryPoint;
    }

    /// Encoder Password
    @Bean
    public  static PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }

    /// การยืนยันตัวตน
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /// ใครเข้า endpoint ไหนได้บ้าง และต้อง login อย่างไร
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customEntryPoint)
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
