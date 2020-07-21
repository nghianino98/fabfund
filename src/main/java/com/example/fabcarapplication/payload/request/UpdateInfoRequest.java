package com.example.fabcarapplication.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInfoRequest {

  @NotBlank
  @Size(min = 4, max = 40)
  private String name;

  @Size(min = 0, max = 20)
  private String password;

}
