package com.myl117.authservice.authservice.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

  private final JdbcTemplate jdbcTemplate;
  private final JwtService jwtService;
  private final EmailService emailService;

  public PasswordResetService(JdbcTemplate jdbcTemplate, JwtService jwtService, EmailService emailService) {
    this.jdbcTemplate = jdbcTemplate;
    this.jwtService = jwtService;
    this.emailService = emailService;
  }

  public void initiatePasswordReset(String email) {
    try {
      System.out.println("Initiating password reset for: " + email);

      // check if user exists
      Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM authservice_users WHERE email = ?",
        Integer.class,
        email
      );

      if (count == null || count == 0) {
        System.out.println("No user found with email: " + email);
        // do not reveal to client whether user exists
        return;
      }

      String token = jwtService.generateToken(email);
      System.out.println("Generated reset token: " + token);

      emailService.sendPasswordResetEmail(email, token);
      System.out.println("Password reset email sent to: " + email);

    } catch (Exception e) {
      System.err.println("Error during password reset for " + email + ": " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Failed to process password reset request");
    }
  }
}
