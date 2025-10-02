package com.myl117.authservice.authservice.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myl117.authservice.authservice.dto.SignupRequest;
import com.myl117.authservice.authservice.exception.UserAlreadyExistsException;

@Service
public class SignupService {

    private final EmailService emailService;
  private final JdbcTemplate jdbcTemplate;
  private final JwtService jwtService;

  public SignupService(JdbcTemplate jdbcTemplate, JwtService jwtService, EmailService emailService) {
    this.jdbcTemplate = jdbcTemplate;
    this.jwtService = jwtService;
    this.emailService = emailService;
  }

  public void Signup(SignupRequest request) {
    System.out.println("test: " + request.getEmail());
    
    Integer count = jdbcTemplate.queryForObject(
      "SELECT COUNT(*) FROM authservice_users WHERE email = ?",
      Integer.class,
      request.getEmail()
    );

    System.out.println("Users count: " + count);

    if (count != null && count > 0) {
      throw new UserAlreadyExistsException("Email is already in use");
    }

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashedPassword = passwordEncoder.encode(request.getPassword());

    String sql = "INSERT INTO authservice_users (name, email, password, status) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql, request.getName(), request.getEmail(), hashedPassword, "PENDING_VERIFICATION");

    String token = jwtService.generateToken(request.getEmail());
    emailService.sendVerificationEmail(request.getName(), request.getEmail(), token);
  }
}
