package com.myl117.authservice.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myl117.authservice.authservice.dto.ChangePasswordRequest;
import com.myl117.authservice.authservice.service.ChangePasswordService;
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

@DisplayName("ChangePasswordController Unit Tests")
class ChangePasswordControllerTests {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock
  private ChangePasswordService changePasswordService;

  private ChangePasswordController changePasswordController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    changePasswordController = new ChangePasswordController(changePasswordService);
    mockMvc = MockMvcBuilders.standaloneSetup(changePasswordController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should return 200 when password is changed successfully")
  void shouldReturn200WhenPasswordChangedSuccessfully() throws Exception {
    ChangePasswordRequest req = new ChangePasswordRequest();
    req.setNewPassword("newSecurePassword123");

    doNothing().when(changePasswordService).changePassword(eq("valid-token"), eq("newSecurePassword123"));

    mockMvc.perform(post("/api/auth/changepassword")
        .param("token", "valid-token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(content().string("Password updated successfully."));
  }

  @Test
  @DisplayName("Should return 400 when token is invalid")
  void shouldReturn400WhenTokenIsInvalid() throws Exception {
    ChangePasswordRequest req = new ChangePasswordRequest();
    req.setNewPassword("newSecurePassword123");

    doThrow(new IllegalArgumentException("Invalid or expired token"))
      .when(changePasswordService).changePassword(eq("bad-token"), anyString());

    mockMvc.perform(post("/api/auth/changepassword")
        .param("token", "bad-token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isBadRequest())
      .andExpect(content().string("Invalid or expired token"));
  }

  @Test
  @DisplayName("Should return 500 when an unexpected error occurs")
  void shouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
    ChangePasswordRequest req = new ChangePasswordRequest();
    req.setNewPassword("newSecurePassword123");

    doThrow(new RuntimeException("Database connection lost"))
      .when(changePasswordService).changePassword(eq("valid-token"), anyString());

    mockMvc.perform(post("/api/auth/changepassword")
        .param("token", "valid-token")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isInternalServerError())
      .andExpect(content().string("Error while updating password."));
  }
}
