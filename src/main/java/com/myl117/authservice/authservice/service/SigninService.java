package com.myl117.authservice.authservice.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myl117.authservice.authservice.dto.SigninRequest;

import java.util.Map;

@Service
public class SigninService {

  private final JdbcTemplate jdbcTemplate;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public SigninService(JdbcTemplate jdbcTemplate, JwtService jwtService) {
    this.jdbcTemplate = jdbcTemplate;
    this.jwtService = jwtService;
  }

  public String signin(SigninRequest request) {
    try {
      Map<String, Object> user = jdbcTemplate.queryForMap(
        "SELECT email, password FROM authservice_users WHERE email = ? AND status = ?",
        request.getEmail(),
        "ACTIVE"
      );

      String storedHashedPassword = (String) user.get("password");
      if (!passwordEncoder.matches(request.getPassword(), storedHashedPassword)) {
        throw new IllegalArgumentException("Invalid email or password.");
      }

      return jwtService.generateToken(request.getEmail());

    } catch (org.springframework.dao.EmptyResultDataAccessException e) {
      throw new IllegalArgumentException("Invalid email or password or account not active.");
    }
  }
}
