package org.example.repository;

import org.example.domain.User;

import java.util.Optional;

// usernameOrEmail 로 로그인 할 수 있는 Custom Query
public interface CustomUserRepository {
    Optional<User> findByUsernameOrEmail(String input);
}
