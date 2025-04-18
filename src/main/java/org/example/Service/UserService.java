package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.domain.Balance;
import org.example.domain.User;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.jwt.JwtUtil;
import org.example.repository.BalanceRepository;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

        Balance balance = new Balance(user, 0L);
        balanceRepository.save(balance);
    }

    // 로그인 후 JWT 토큰 발급
    public String loginAndGetToken(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getLogin());

        if (userOpt.isPresent() && passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            return jwtUtil.generateToken(user.getUsername()); // 사용자 username 으로 JWT 토큰생성
        }

        return null;
    }
}
