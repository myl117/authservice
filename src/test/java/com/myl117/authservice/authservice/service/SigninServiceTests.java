package com.myl117.authservice.authservice.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.myl117.authservice.authservice.dto.SigninRequest;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
@DisplayName("SigninService Unit Tests")
public class SigninServiceTests {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private JwtService jwtService;

  private SigninService signinService;

  @BeforeEach
  void setUp() {
    signinService = new SigninService(jdbcTemplate, jwtService);
  }

  @Test
  @DisplayName("Should return JWT token when credentials are valid")
  void shouldReturnTokenWhenCredentialsValid() {
    String rawPassword = "correctpassword";
    String hashed = new BCryptPasswordEncoder().encode(rawPassword);

    SigninRequest req = new SigninRequest();
    req.setEmail("john.doe@example.com");
    req.setPassword(rawPassword);

    when(jdbcTemplate.queryForMap(anyString(), eq("john.doe@example.com"), eq("ACTIVE")))
      .thenReturn(Map.of("email", "john.doe@example.com", "password", hashed));
    when(jwtService.generateToken("john.doe@example.com")).thenReturn("mocked-token");

    String token = signinService.signin(req);

    assertThat(token).isEqualTo("mocked-token");
    verify(jwtService).generateToken("john.doe@example.com");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when password is wrong")
  void shouldThrowWhenPasswordIsIncorrect() {
    String hashed = new BCryptPasswordEncoder().encode("correctpassword");

    SigninRequest req = new SigninRequest();
    req.setEmail("john.doe@example.com");
    req.setPassword("wrongpassword");

    when(jdbcTemplate.queryForMap(anyString(), eq("john.doe@example.com"), eq("ACTIVE")))
      .thenReturn(Map.of("email", "john.doe@example.com", "password", hashed));

    assertThatThrownBy(() -> signinService.signin(req))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Invalid email or password.");
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when user not found or not active")
  void shouldThrowWhenUserNotFoundOrNotActive() {
    SigninRequest req = new SigninRequest();
    req.setEmail("nobody@example.com");
    req.setPassword("somepassword");

    when(jdbcTemplate.queryForMap(anyString(), eq("nobody@example.com"), eq("ACTIVE")))
      .thenThrow(new EmptyResultDataAccessException(1));

    assertThatThrownBy(() -> signinService.signin(req))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Invalid email or password or account not active.");
  }
}
