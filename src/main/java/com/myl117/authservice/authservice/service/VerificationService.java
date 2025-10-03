package com.myl117.authservice.authservice.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
  private JwtService jwtService;
  private JdbcTemplate jdbcTemplate;

  public VerificationService(JwtService jwtService, JdbcTemplate jdbcTemplate) {
    this.jwtService = jwtService;
    this.jdbcTemplate = jdbcTemplate;
  }

  public boolean verifyToken(String token) {
    try {
      String email = jwtService.extractEmail(token);
      int updated = jdbcTemplate.update("UPDATE authservice_users SET status = ? WHERE email = ? AND status = ?",
        "ACTIVE",
        email,
        "PENDING_VERIFICATION"
      );

      return updated > 0;
    } catch(Exception e) {
      return false;
    }
  }
}
