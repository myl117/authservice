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
@DisplayName("VerificationService Unit Tests")
public class VerificationServiceTests {

  @Mock
  private JwtService jwtService;

  @Mock
  private JdbcTemplate jdbcTemplate;

  private VerificationService verificationService;

  @BeforeEach
  void setUp() {
    verificationService = new VerificationService(jwtService, jdbcTemplate);
  }

  @Test
  @DisplayName("Should return true and activate user when token is valid")
  void shouldReturnTrueAndActivateUserWhenTokenValid() {
    when(jwtService.extractEmail("valid-token")).thenReturn("john.doe@example.com");
    when(jdbcTemplate.update(anyString(), eq("ACTIVE"), eq("john.doe@example.com"), eq("PENDING_VERIFICATION")))
      .thenReturn(1);

    boolean result = verificationService.verifyToken("valid-token");

    assertThat(result).isTrue();
    verify(jdbcTemplate).update(anyString(), eq("ACTIVE"), eq("john.doe@example.com"), eq("PENDING_VERIFICATION"));
  }

  @Test
  @DisplayName("Should return false when no rows were updated (user already active or not found)")
  void shouldReturnFalseWhenNoRowsUpdated() {
    when(jwtService.extractEmail("valid-token")).thenReturn("john.doe@example.com");
    when(jdbcTemplate.update(anyString(), eq("ACTIVE"), eq("john.doe@example.com"), eq("PENDING_VERIFICATION")))
      .thenReturn(0);

    boolean result = verificationService.verifyToken("valid-token");

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Should return false when JWT parsing throws an exception")
  void shouldReturnFalseWhenJwtParsingFails() {
    when(jwtService.extractEmail("bad-token")).thenThrow(new RuntimeException("Invalid JWT"));

    boolean result = verificationService.verifyToken("bad-token");

    assertThat(result).isFalse();
    verify(jdbcTemplate, never()).update(anyString(), any(), any(), any());
  }
}
