package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 Email입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), request.getEmail(), encodedPassword);
        userRepository.save(user);
    }

    // 로그인 시 비밀번호 비교
    public boolean login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getLogin());

        return userOpt.isPresent() && passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword());
    }
}
