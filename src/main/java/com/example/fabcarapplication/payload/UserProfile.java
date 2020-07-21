package com.example.fabcarapplication.payload;

import com.example.fabcarapplication.model.user.Role;
import com.example.fabcarapplication.model.user.RoleName;
import java.time.Instant;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
  private Long id;
  private String username;
  private String name;
  private Set<Role> roles;
  private String email;
  private Instant joinedAt;
  private Instant updatedAt;

  public UserProfile(Long id, String username, String name,
      Set<Role> roles, String email, Instant joinedAt, Instant updatedAt) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.roles = roles;
    this.email = email;
    this.joinedAt = joinedAt;
    this.updatedAt = updatedAt;
  }
}