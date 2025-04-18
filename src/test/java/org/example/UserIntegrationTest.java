package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.user.LoginRequest;
import org.example.dto.user.RegisterRequest;
import org.example.repository.BalanceRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BalanceRepository balanceRepository;

    @BeforeEach
    void clearDatabase() {
        balanceRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입, 로그인, Balance API ✅")
    @Transactional
    @Rollback(false)
    void loginTest() throws Exception {
        String username = "testuser";
        String email = "testuser@gmail.com";
        String password = "testuser1234";

        RegisterRequest register = new RegisterRequest(username, email, password);
        mockMvc.perform(post("/api/users/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                    .andDo(print())
                    .andExpect(status().isOk());

        LoginRequest UsernameLogin = new LoginRequest(username, password);
        mockMvc.perform(post("/api/users/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UsernameLogin)))
                    .andDo(print())
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        LoginRequest loginEmail = new LoginRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/users/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginEmail)))
                        .andDo(print())
                        .andReturn();
        String token = result.getResponse().getContentAsString();

        // Balance
        String initialBalanceStr = mockMvc.perform(get("/api/balance").header("Authorization", "Bearer " + token)
                        .param("username", username))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        Long initialBalance = Long.parseLong(initialBalanceStr);

        mockMvc.perform(post("/api/balance/add").with(csrf()).header("Authorization", "Bearer " + token)
                .param("username", username)
                .param("amount", "50000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("자산 추가 완료"));

        String addBalanceStr = mockMvc.perform(get("/api/balance").header("Authorization", "Bearer " + token)
                .param("username", username))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Long addBalance = Long.parseLong(addBalanceStr);
        assertEquals(initialBalance + 50000, addBalance);

        mockMvc.perform(post("/api/balance/subtract").with(csrf()).header("Authorization", "Bearer " + token)
                .param("username", username)
                .param("amount", "40000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("자산 차감 완료"));

        String subtractBalanceStr = mockMvc.perform(get("/api/balance").header("Authorization", "Bearer " + token)
                .param("username", username))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Long subtractBalance = Long.parseLong(subtractBalanceStr);
        assertEquals(addBalance - 40000, subtractBalance);
    }
}
