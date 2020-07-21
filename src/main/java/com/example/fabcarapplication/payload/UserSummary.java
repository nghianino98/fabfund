package com.example.fabcarapplication.payload;

import com.example.fabcarapplication.model.user.Role;
import com.example.fabcarapplication.model.user.RoleName;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class UserSummary {

  private Long id;
  private String username;
  private String name;
  private Set<String> roles;
  private String email;

  public UserSummary(Long id, String username, String name, Set<String> roles,
      String email) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.roles = roles;
    this.email = email;
  }
}