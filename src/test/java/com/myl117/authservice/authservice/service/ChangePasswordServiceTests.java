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
@DisplayName("ChangePasswordService Unit Tests")
public class ChangePasswordServiceTests {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private JwtService jwtService;

  private ChangePasswordService changePasswordService;

  @BeforeEach
  void setUp() {
    changePasswordService = new ChangePasswordService(jdbcTemplate, jwtService);
  }

  @Test
  @DisplayName("Should update hashed password when token is valid")
  void shouldUpdateHashedPasswordWhenTokenIsValid() {
    when(jwtService.extractEmail("valid-token")).thenReturn("john.doe@example.com");

    changePasswordService.changePassword("valid-token", "newSecurePassword");

    // Verify DB update was called with hashed password (not plain text)
    verify(jdbcTemplate).update(
      anyString(),
      argThat(hashed -> hashed instanceof String && !hashed.equals("newSecurePassword")),
      eq("john.doe@example.com")
    );
  }

  @Test
  @DisplayName("Should throw RuntimeException when token extraction fails")
  void shouldThrowWhenTokenExtractionFails() {
    when(jwtService.extractEmail("bad-token")).thenThrow(new RuntimeException("JWT parsing failed"));

    assertThatThrownBy(() -> changePasswordService.changePassword("bad-token", "newPassword"))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Failed to change password");

    verify(jdbcTemplate, never()).update(anyString(), any(), any());
  }

  @Test
  @DisplayName("Should throw RuntimeException when email extracted is null")
  void shouldThrowWhenEmailIsNull() {
    when(jwtService.extractEmail("token-null-email")).thenReturn(null);

    assertThatThrownBy(() -> changePasswordService.changePassword("token-null-email", "newPassword"))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Failed to change password");

    verify(jdbcTemplate, never()).update(anyString(), any(), any());
  }

  @Test
  @DisplayName("Should throw RuntimeException when DB update fails")
  void shouldThrowWhenDbUpdateFails() {
    when(jwtService.extractEmail("valid-token")).thenReturn("john.doe@example.com");
    when(jdbcTemplate.update(anyString(), any(), any()))
      .thenThrow(new RuntimeException("Database error"));

    assertThatThrownBy(() -> changePasswordService.changePassword("valid-token", "newPassword"))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("Failed to change password");
  }
}
