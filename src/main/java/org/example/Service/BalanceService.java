package org.example.Service;

import lombok.RequiredArgsConstructor;
import org.example.domain.Balance;
import org.example.domain.User;
import org.example.repository.BalanceRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    // Amount
    public Long getBalance(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        return balanceRepository.findByUser(user).orElseThrow(() -> new IllegalStateException("잔고를 찾을 수 없습니다")).getAmount();
    }

    // Reset Amount
    @Transactional
    public void initBalance(User user, Long initalAmount) {
        Balance balance = new Balance();
        balance.setUser(user);
        balance.setAmount(initalAmount);
        balanceRepository.save(balance);
    }

    // Add Amount
    @Transactional
    public void addBalance(String username, Long amount) {
        Balance balance = getBalanceEntity(username);
        balance.setAmount(balance.getAmount() + amount);
    }

    // Subtract Amount
    @Transactional
    public void subtractBalance(String username, Long amount) {
        Balance balance = getBalanceEntity(username);

        if (balance.getAmount() < amount) {
            throw new IllegalArgumentException("잔고가 부족합니다");
        }
        balance.setAmount(balance.getAmount() - amount);
    }

    // 내부 공용 Method
    private Balance getBalanceEntity(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        return balanceRepository.findByUser(user).orElseThrow(() -> new IllegalStateException("잔고 정보가 없습니다"));
    }
}
