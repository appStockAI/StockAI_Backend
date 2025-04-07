package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 Email입니다.");
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        userRepository.save(user);
    }

    public boolean login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getLogin());

        return userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword());
    }
}
