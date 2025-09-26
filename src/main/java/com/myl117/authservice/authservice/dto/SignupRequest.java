package com.myl117.authservice.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password should be a minimum of 6 characters")
  private String password;

  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getPassword() { return password; }

  public void setName(String name) { this.name = name; }
  public void setEmail(String email) { this.email = email; }
  public void setPassword(String password) { this.password = password; }
}
