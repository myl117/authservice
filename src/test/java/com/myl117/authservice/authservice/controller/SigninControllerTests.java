package com.myl117.authservice.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myl117.authservice.authservice.dto.SigninRequest;
import com.myl117.authservice.authservice.service.SigninService;
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

@DisplayName("SigninController Unit Tests")
class SigninControllerTests {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock
  private SigninService signinService;

  private SigninController signinController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    signinController = new SigninController(signinService);
    mockMvc = MockMvcBuilders.standaloneSetup(signinController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should return 200 and token when signin is successful")
  void shouldReturn200WithTokenWhenSigninSuccessful() throws Exception {
    SigninRequest req = new SigninRequest();
    req.setEmail("john.doe@example.com");
    req.setPassword("totallysecurepassword");

    when(signinService.signin(any(SigninRequest.class))).thenReturn("mocked-jwt-token");

    mockMvc.perform(post("/api/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(content().string("Successfully signed in. Here is your token: mocked-jwt-token"));
  }

  @Test
  @DisplayName("Should return 401 when credentials are invalid")
  void shouldReturn401WhenCredentialsInvalid() throws Exception {
    SigninRequest req = new SigninRequest();
    req.setEmail("john.doe@example.com");
    req.setPassword("wrongpassword");

    when(signinService.signin(any(SigninRequest.class)))
      .thenThrow(new IllegalArgumentException("Invalid email or password."));

    mockMvc.perform(post("/api/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isUnauthorized())
      .andExpect(content().string("Invalid email or password."));
  }

  @Test
  @DisplayName("Should return 401 when account is not active")
  void shouldReturn401WhenAccountNotActive() throws Exception {
    SigninRequest req = new SigninRequest();
    req.setEmail("pending@example.com");
    req.setPassword("somepassword");

    when(signinService.signin(any(SigninRequest.class)))
      .thenThrow(new IllegalArgumentException("Invalid email or password or account not active."));

    mockMvc.perform(post("/api/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isUnauthorized())
      .andExpect(content().string("Invalid email or password or account not active."));
  }
}
