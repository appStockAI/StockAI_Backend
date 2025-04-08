package org.example.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 로그인 시 "usernameOrEmail + Password" 담는 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String login; //username & email
    private String password;
}
