package com.myl117.authservice.authservice.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService {

  private final JdbcTemplate jdbcTemplate;
  private final JwtService jwtService;

  public ChangePasswordService(JdbcTemplate jdbcTemplate, JwtService jwtService) {
    this.jdbcTemplate = jdbcTemplate;
    this.jwtService = jwtService;
  }

  public void changePassword(String token, String newPassword) {
    try {
      System.out.println("Changing password for token: " + token);

      String email = jwtService.extractEmail(token); 
      if (email == null || email.isEmpty()) {
        throw new IllegalArgumentException("Invalid or expired token");
      }

      // Hash new password
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      String hashedPassword = encoder.encode(newPassword);

      jdbcTemplate.update(
        "UPDATE authservice_users SET password = ? WHERE email = ?",
        hashedPassword, email
      );

      System.out.println("Password successfully updated for " + email);

    } catch (Exception e) {
      System.err.println("Error changing password: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Failed to change password");
    }
  }
}
