package com.myl117.authservice.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myl117.authservice.authservice.dto.SigninRequest;
import com.myl117.authservice.authservice.service.SigninService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class SigninController {

  private final SigninService signinService;

  public SigninController(SigninService signinService) {
    this.signinService = signinService;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest request) {
    try {
      String token = signinService.signin(request);
      return ResponseEntity.ok("Successfully signed in. Here is your token: " + token);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }
}
