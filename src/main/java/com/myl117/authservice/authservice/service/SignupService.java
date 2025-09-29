package com.myl117.authservice.authservice.service;

import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myl117.authservice.authservice.dto.SignupRequest;

@Service
public class SignupService {
  private final JdbcTemplate jdbcTemplate;

  public SignupService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void Signup(SignupRequest request) {
    System.out.println("test: " + request.getEmail());
    
    try {
      Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM authservice_users WHERE email = ?",
        Integer.class,
        request.getEmail()
      );

      System.out.println("Users count: " + count);

      if (count != null && count > 0) {
        throw new RuntimeException("Email already in use");
      }
    } catch (Exception e) {
      System.out.println("Exception in Signup method: " + e.getMessage());
    }

    // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // String hashedPassword = passwordEncoder.encode(request.getPassword());

    // String sql = "INSERT INTO authservice_users (name, email, password) VALUES (?, ?, ?)";
    // jdbcTemplate.update(sql, request.getName(), request.getEmail(), hashedPassword);
  }
}
