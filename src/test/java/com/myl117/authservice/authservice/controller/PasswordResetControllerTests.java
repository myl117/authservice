package com.myl117.authservice.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myl117.authservice.authservice.dto.PasswordResetRequest;
import com.myl117.authservice.authservice.service.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PasswordResetController Unit Tests")
class PasswordResetControllerTests {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock
  private PasswordResetService passwordResetService;

  private PasswordResetController passwordResetController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    passwordResetController = new PasswordResetController(passwordResetService);
    mockMvc = MockMvcBuilders.standaloneSetup(passwordResetController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should return 200 when password reset email is sent")
  void shouldReturn200WhenResetEmailSent() throws Exception {
    PasswordResetRequest req = new PasswordResetRequest();
    req.setEmail("john.doe@example.com");

    doNothing().when(passwordResetService).initiatePasswordReset(eq("john.doe@example.com"));

    mockMvc.perform(post("/api/auth/reset")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(content().string("Password reset email sent if the account exists."));
  }

  @Test
  @DisplayName("Should return 200 even when user does not exist (security: no user enumeration)")
  void shouldReturn200WhenUserDoesNotExist() throws Exception {
    PasswordResetRequest req = new PasswordResetRequest();
    req.setEmail("unknown@example.com");

    // Service silently returns without sending email when user not found
    doNothing().when(passwordResetService).initiatePasswordReset(eq("unknown@example.com"));

    mockMvc.perform(post("/api/auth/reset")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(content().string("Password reset email sent if the account exists."));
  }

  @Test
  @DisplayName("Should return 500 when an unexpected error occurs")
  void shouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
    PasswordResetRequest req = new PasswordResetRequest();
    req.setEmail("john.doe@example.com");

    doThrow(new RuntimeException("Mail server unavailable"))
      .when(passwordResetService).initiatePasswordReset(anyString());

    mockMvc.perform(post("/api/auth/reset")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isInternalServerError())
      .andExpect(content().string("Error processing password reset request"));
  }
}
