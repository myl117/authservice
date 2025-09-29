package com.myl117.authservice.authservice.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myl117.authservice.authservice.dto.SignupRequest;
import com.myl117.authservice.authservice.service.SignupService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class SignupController {
  private final  SignupService signupService;

  public SignupController(SignupService signupService) {
    this.signupService = signupService;
  }

  @PostMapping("/signup")
  public String signup(@Valid @RequestBody SignupRequest request) {
    System.out.println("Controller hit");
      signupService.Signup(request);
      return "Got in";
  }
}
