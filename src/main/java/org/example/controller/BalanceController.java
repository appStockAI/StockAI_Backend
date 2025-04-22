package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.Service.BalanceService;
import org.example.domain.User;
import org.example.dto.balance.BalanceResponse;
import org.example.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balance")
public class BalanceController {
    private final BalanceService balanceService;
    private final UserRepository userRepository;

    private ResponseEntity<?> withUser(String username, java.util.function.Function<User, ResponseEntity<?>> action) {
        return userRepository.findByUsername(username).map(action).orElse(ResponseEntity.badRequest().body("유저가 존재하지 않습니다"));
    }

    // get : /api/balance?username=someUser
    @GetMapping
    public ResponseEntity<?> getBalance(@RequestParam String username) {
        return withUser(username, user -> {
            Long amount = balanceService.getBalance(username);
            return ResponseEntity.ok(new BalanceResponse(amount));
        });
    }

    // post : /api/balance/add
    @PostMapping("/add")
    public ResponseEntity<?> addBalance(@RequestParam String username, @RequestParam Long amount) {
        return withUser(username, user -> {
            balanceService.addBalance(username, amount);
            return ResponseEntity.ok("자산 추가 완료");
        });
    }

    // post : /api/balance/subtract
    @PostMapping("/subtract")
    public ResponseEntity<?> subtractBalance(@RequestParam String username, @RequestParam Long amount) {
        return withUser(username, user -> {
            balanceService.subtractBalance(username, amount);
            return ResponseEntity.ok("자산 차감 완료");
        });
    }
}
