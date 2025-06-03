package com.authentication.authentication.repository;

import com.authentication.authentication.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Optional<UserModel> findByUsernameOrEmail(String username, String email);
}
