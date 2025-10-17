package com.myl117.authservice.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password should be a minimum of 6 characters")
  private String newPassword;

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
