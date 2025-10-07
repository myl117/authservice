package com.myl117.authservice.authservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.myl117.authservice.authservice.dto.SignupRequest;
import com.myl117.authservice.authservice.exception.UserAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignupService Unit Tests")
public class SignupServiceTests {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private EmailService emailService;

  private JwtService jwtService;
  
  private SignupService signupService;

   @BeforeEach
    void setUp() {
      jwtService = new JwtService();
      signupService = new SignupService(jdbcTemplate, jwtService, emailService);
    }

  @Test
  @DisplayName("Should successfully signup a new user with hashed password")
  void shouldSignupUserSuccessfully() {
    SignupRequest req = new SignupRequest();
    req.setName("John");
    req.setEmail("john.doe@example.com");
    req.setPassword("totallysecurepassword");

    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(0);

    signupService.Signup(req);

    verify(jdbcTemplate).update(anyString(), eq("John"), eq("john.doe@example.com"), anyString(), eq("PENDING_VERIFICATION"));
    verify(emailService).sendVerificationEmail(eq("John"),  eq("john.doe@example.com"), anyString());
  }

  @Test
  @DisplayName("Should throw UserAlreadyExistsException if email is already in use")
  void shouldThrowIfUserAlreadyExists() {
    SignupRequest req = new SignupRequest();
    req.setEmail("exists@example.com");

    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(1);

    assertThatThrownBy(() -> signupService.Signup(req))
      .isInstanceOf(UserAlreadyExistsException.class)
      .hasMessage("Email is already in use");
  }
}