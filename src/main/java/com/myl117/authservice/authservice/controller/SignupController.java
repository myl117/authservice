package com.myl117.authservice.authservice.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myl117.authservice.authservice.dto.SignupRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class SignupController {
  @PostMapping("/signup")
  public String signup(@Valid @RequestBody SignupRequest request) {
      return "Got in";
  }
}
