package com.example.fabcarapplication.controller.user;

import com.example.fabcarapplication.payload.UserIdentityAvailability;
import com.example.fabcarapplication.payload.UserSummary;
import com.example.fabcarapplication.repository.UserRepository;
import com.example.fabcarapplication.security.CurrentUser;
import com.example.fabcarapplication.security.UserPrincipal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @GetMapping("/me")
  @Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON", "ROLE_USER"})
  public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {

    Set<String> roles = new HashSet<>();

    Collection<GrantedAuthority> collectionRoles = (Collection<GrantedAuthority>) currentUser.getAuthorities();

    Iterator iterator = collectionRoles.iterator();

    while (iterator.hasNext()){

      GrantedAuthority grantedAuthority = (GrantedAuthority) iterator.next();

      roles.add(grantedAuthority.getAuthority());

    }

    UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName()
        , roles, currentUser.getEmail());

    return userSummary;
  }

  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
    Boolean isAvailable = !userRepository.existsByUsername(username);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
    Boolean isAvailable = !userRepository.existsByEmail(email);
    return new UserIdentityAvailability(isAvailable);
  }

}
