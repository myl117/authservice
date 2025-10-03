package com.myl117.authservice.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myl117.authservice.authservice.service.VerificationService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/auth")
public class VerifyController {
  private final VerificationService verificationService;

  public VerifyController(VerificationService verificationService) {
    this.verificationService = verificationService;
  }
  
  @GetMapping("/verify")  
  public ResponseEntity<String> verify(@RequestParam("token") String token) {
    boolean success = verificationService.verifyToken(token);

    if (success) {
      return ResponseEntity.ok("Successfully verified email");
    } else {
      return ResponseEntity.badRequest().body("Invalid or expired token");
    }
  }
}
