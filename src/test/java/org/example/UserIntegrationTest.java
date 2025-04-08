package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("로그인 ✅")
    @Transactional
    @Rollback(false)
    void loginTest() throws Exception {
        RegisterRequest register = new RegisterRequest("testuser", "testuser@gmail.com", "testuser1234");

        mockMvc.perform(post("/api/users/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                    .andDo(print())
                    .andExpect(status().isOk());

        LoginRequest login = new LoginRequest("testuser", "testuser1234");

        mockMvc.perform(post("/api/users/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("로그인 성공"));
    }
}
