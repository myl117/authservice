package com.myl117.authservice.authservice.controller;

import com.myl117.authservice.authservice.service.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("VerifyController Unit Tests")
class VerifyControllerTests {

  private MockMvc mockMvc;

  @Mock
  private VerificationService verificationService;

  private VerifyController verifyController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    verifyController = new VerifyController(verificationService);
    mockMvc = MockMvcBuilders.standaloneSetup(verifyController).build();
  }

  @Test
  @DisplayName("Should return 200 when token is valid and email is verified")
  void shouldReturn200WhenTokenValid() throws Exception {
    when(verificationService.verifyToken("valid-token")).thenReturn(true);

    mockMvc.perform(get("/api/auth/verify")
        .param("token", "valid-token"))
      .andExpect(status().isOk())
      .andExpect(content().string("Successfully verified email"));
  }

  @Test
  @DisplayName("Should return 400 when token is invalid or expired")
  void shouldReturn400WhenTokenInvalid() throws Exception {
    when(verificationService.verifyToken("bad-token")).thenReturn(false);

    mockMvc.perform(get("/api/auth/verify")
        .param("token", "bad-token"))
      .andExpect(status().isBadRequest())
      .andExpect(content().string("Invalid or expired token"));
  }
}
