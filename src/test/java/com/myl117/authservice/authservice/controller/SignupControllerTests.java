package com.myl117.authservice.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myl117.authservice.authservice.dto.SignupRequest;
import com.myl117.authservice.authservice.exception.UserAlreadyExistsException;
import com.myl117.authservice.authservice.service.SignupService;
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

@DisplayName("SignupController Unit Tests")
class SignupControllerTest {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock
  private SignupService signupService;

  private SignupController signupController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    signupController = new SignupController(signupService);
    mockMvc = MockMvcBuilders.standaloneSetup(signupController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should return 200 when signup is successful")
  void shouldReturn200WhenSignupSuccessful() throws Exception {
    SignupRequest req = new SignupRequest();
    req.setName("John");
    req.setEmail("john.doe@example.com");
    req.setPassword("totallysecurepassword");


    doNothing().when(signupService).Signup(req);

    mockMvc.perform(post("/api/auth/signup")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(content().string("Successfully created user"));
  }

  @Test
  @DisplayName("Should return 409 when user already exists")
  void shouldReturn409WhenUserAlreadyExists() throws Exception {
    SignupRequest req = new SignupRequest();
    req.setName("Jane");
    req.setEmail("jane@example.com");
    req.setPassword("janestotallysecurepassword");

    doThrow(new UserAlreadyExistsException("Email is already in use")).when(signupService).Signup(any(SignupRequest.class));

    mockMvc.perform(post("/api/auth/signup")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isConflict())
      .andExpect(content().string("Email is already in use"));
  }
}
