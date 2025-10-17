package com.myl117.authservice.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myl117.authservice.authservice.dto.ChangePasswordRequest;
import com.myl117.authservice.authservice.service.ChangePasswordService;

@RestController
@RequestMapping("/api/auth")
public class ChangePasswordController {

  private final ChangePasswordService changePasswordService;

  public ChangePasswordController(ChangePasswordService changePasswordService) {
    this.changePasswordService = changePasswordService;
  }

  @PostMapping("/changepassword")
  public ResponseEntity<String> changePassword(
      @RequestParam("token") String token,
      @RequestBody ChangePasswordRequest request
  ) {
    try {
      changePasswordService.changePassword(token, request.getNewPassword());
      return ResponseEntity.ok("Password updated successfully.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("Error while updating password.");
    }
  }
}
