package com.myl117.authservice.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myl117.authservice.authservice.dto.PasswordResetRequest;
import com.myl117.authservice.authservice.service.PasswordResetService;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  public PasswordResetController(PasswordResetService passwordResetService) {
    this.passwordResetService = passwordResetService;
  }

  @PostMapping("/reset")
  public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
    try {
      passwordResetService.initiatePasswordReset(request.getEmail());
      return ResponseEntity.ok("Password reset email sent if the account exists.");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("Error processing password reset request");
    }
  }
}
