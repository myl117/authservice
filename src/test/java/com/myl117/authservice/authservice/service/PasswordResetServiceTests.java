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
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordResetService Unit Tests")
public class PasswordResetServiceTests {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private JwtService jwtService;

  @Mock
  private EmailService emailService;

  private PasswordResetService passwordResetService;

  @BeforeEach
  void setUp() {
    passwordResetService = new PasswordResetService(jdbcTemplate, jwtService, emailService);
  }

  @Test
  @DisplayName("Should generate token and send reset email when user exists")
  void shouldSendResetEmailWhenUserExists() {
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("john.doe@example.com")))
      .thenReturn(1);
    when(jwtService.generateToken("john.doe@example.com")).thenReturn("reset-token");

    passwordResetService.initiatePasswordReset("john.doe@example.com");

    verify(jwtService).generateToken("john.doe@example.com");
    verify(emailService).sendPasswordResetEmail(eq("john.doe@example.com"), eq("reset-token"));
  }

  @Test
  @DisplayName("Should do nothing (silently) when user does not exist")
  void shouldDoNothingWhenUserDoesNotExist() {
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("unknown@example.com")))
      .thenReturn(0);

    passwordResetService.initiatePasswordReset("unknown@example.com");

    verify(jwtService, never()).generateToken(anyString());
    verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
  }

  @Test
  @DisplayName("Should do nothing (silently) when count query returns null")
  void shouldDoNothingWhenCountIsNull() {
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("null@example.com")))
      .thenReturn(null);

    passwordResetService.initiatePasswordReset("null@example.com");

    verify(jwtService, never()).generateToken(anyString());
    verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
  }

  @Test
  @DisplayName("Should throw RuntimeException when email service fails")
  void shouldThrowWhenEmailServiceFails() {
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("john.doe@example.com")))
      .thenReturn(1);
    when(jwtService.generateToken("john.doe@example.com")).thenReturn("reset-token");
    doThrow(new RuntimeException("SMTP failure"))
      .when(emailService).sendPasswordResetEmail(anyString(), anyString());

    assertThatThrownBy(() -> passwordResetService.initiatePasswordReset("john.doe@example.com"))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Failed to process password reset request");
  }
}
