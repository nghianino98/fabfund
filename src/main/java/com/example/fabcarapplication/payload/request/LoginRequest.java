package com.example.fabcarapplication.payload.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  @NotBlank
  private String usernameOrEmail;

  @NotBlank
  private String password;
}