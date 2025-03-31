package org.example.repository;

import org.example.domain.User;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> findByUsernameOrEmail(String input);
}
