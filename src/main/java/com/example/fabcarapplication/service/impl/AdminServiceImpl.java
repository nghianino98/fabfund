package com.example.fabcarapplication.service.impl;

import com.example.fabcarapplication.config.member.AdminConfig;
import com.example.fabcarapplication.config.member.UserConfig;
import com.example.fabcarapplication.dto.user.Admin;
import com.example.fabcarapplication.exception.AppException;
import com.example.fabcarapplication.exception.ResourceNotFoundException;
import com.example.fabcarapplication.model.user.Role;
import com.example.fabcarapplication.model.user.RoleName;
import com.example.fabcarapplication.model.user.User;
import com.example.fabcarapplication.payload.UserProfile;
import com.example.fabcarapplication.payload.request.SignUpRequest;
import com.example.fabcarapplication.payload.request.UpdateInfoRequest;
import com.example.fabcarapplication.payload.request.UpdateRolesRequest;
import com.example.fabcarapplication.payload.response.ApiResponse;
import com.example.fabcarapplication.repository.RoleRepository;
import com.example.fabcarapplication.repository.UserRepository;
import com.example.fabcarapplication.service.AdminService;
import com.example.fabcarapplication.service.CertificateAuthorityService;
import com.example.fabcarapplication.service.WalletService;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class AdminServiceImpl implements AdminService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AdminServiceImpl.class);

  @Autowired
  AdminConfig adminConfig;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserConfig userConfig;

  @Autowired
  CertificateAuthorityService certificateAuthorityService;

  @Autowired
  WalletService walletService;

  @Override
  public boolean enrollAdmin() {
    String adminName = adminConfig.getUser();

    if (walletService.checkExist(adminName)) {
      LOGGER.info("An identity for the admin user {} already exists in the wallet", adminName);
      return false;
    }
    try {
      Enrollment enrollment = certificateAuthorityService
          .enroll(adminName, adminConfig.getSecret(), adminConfig.getEnrollmentRequest());
      Identity user = Identity
          .createIdentity(adminConfig.getMspId(), enrollment.getCert(), enrollment.getKey());
      if (walletService.put(adminName, user)) {
        LOGGER.info("Successfully enrolled user {} and imported it into the wallet", adminName);
        return true;
      }
    } catch (EnrollmentException | InvalidArgumentException | IOException e) {
      LOGGER.error("{}",e);
    }

    LOGGER.info("Enroll admin failed");
    return false;


  }

  @Override
  public boolean registerUser(String userName, String adminName) {

    // Check to see if we've already enrolled the user.
    boolean userExists = walletService.checkExist(userName);
    if (userExists) {
      LOGGER.info("An identity for the user {} already exists in the wallet", userName);
      return false;
    }

    userExists = walletService.checkExist(adminName);
    if (!userExists) {
      LOGGER.info("{} needs to be enrolled and added to the wallet first", adminName);
      return false;
    }

    try {
      Identity adminIdentity = walletService.get(adminName);
      Admin admin = new Admin(adminIdentity);
      RegistrationRequest registrationRequest = userConfig.getRegistrationRequest(userName);
      Enrollment enrollment = certificateAuthorityService
          .register(registrationRequest, admin, userName);
      Identity user = Identity
          .createIdentity(userConfig.getMspId(), enrollment.getCert(), enrollment.getKey());
      walletService.put(userName, user);
      LOGGER.info(("Successfully enrolled user {} and imported it into the wallet"),
          userName);
      return true;
    } catch (Exception e) {
      LOGGER.error("{}",e);
    }

    return false;
  }

  @Override
  public UserProfile getUserProfileByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
        user.getRoles(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    return userProfile;
  }

  @Override
  public UserProfile getUserProfileById(long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", id));

    UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
        user.getRoles(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    return userProfile;
  }

  @Override
  public List<UserProfile> getAllUserProfiles(int roleId) {
//    List<UserProfile> userProfileList = new ArrayList<>();
//
//    List<User> listUser = userRepository.findAll();
//
//    Iterator iterator = listUser.iterator();
//
//    while (iterator.hasNext()) {
//
//      User user = (User) iterator.next();
//
//      UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
//          user.getRoles(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
//
//      userProfileList.add(userProfile);
//    }
//
//    return userProfileList;
    List<UserProfile> userProfileList = new ArrayList<>();

    List<User> listUser = userRepository.findAll();

    Iterator iterator = listUser.iterator();

    while (iterator.hasNext()) {

      User user = (User) iterator.next();

      UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
          user.getRoles(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());

      Role role = (Role) user.getRoles().toArray()[0];

      if (roleId != 0) {
        if (role.getId() == roleId) {
          userProfileList.add(userProfile);
        }
      }
      else {
        userProfileList.add(userProfile);
      }

    }

    return userProfileList;
  }

  @Override
  public List<String> getAllActorId() {
    List<String> userProfileList = new ArrayList<>();

    List<User> listUser = userRepository.findAll();

    Iterator iterator = listUser.iterator();

    while (iterator.hasNext()) {

      User user = (User) iterator.next();

      UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
          user.getRoles(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());

      Role role = (Role) user.getRoles().toArray()[0];

      if (role.getId()==3 || role.getId() == 4 || role.getId() ==5 )
      {
        userProfileList.add(userProfile.getName());
      }

    }

    return userProfileList;
  }

  @Override
  public List<UserProfile> getAllShareholderProfiles() {
    List<UserProfile> userProfileList = new ArrayList<>();

    List<UserProfile> userProfilesFounder = getAllUserProfilesByRole(3);
    List<UserProfile> userProfilesCommon = getAllUserProfilesByRole(4);
    List<UserProfile> userProfilesPreferred = getAllUserProfilesByRole(5);

    userProfileList.addAll(userProfilesFounder);
    userProfileList.addAll(userProfilesCommon);
    userProfileList.addAll(userProfilesPreferred);

    return userProfileList;
  }

  @Override
  public List<UserProfile> getAllUserProfilesByRole(int idRole) {
    List<UserProfile> userProfileList = new ArrayList<>();

    List<User> listUser = userRepository.findAll();

    Iterator iterator = listUser.iterator();

    while (iterator.hasNext()) {

      boolean get = false;

      User user = (User) iterator.next();

      Set<Role> setUserRole = user.getRoles();

      Iterator iterator1 = setUserRole.iterator();

      Role role = (Role) setUserRole.toArray()[0];

        if (role.getId() == idRole) {
          get = true;
        }

//      while (iterator1.hasNext()) {
//
//        Role role = (Role) iterator.next();
//
//        if (role.getId() == idRole) {
//          get = true;
//        }
//
//      }

      if (get == true) {

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
            user.getRoles(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());

        userProfileList.add(userProfile);
      }
    }

    return userProfileList;
  }

  @Override
  public ResponseEntity<?> addUserFromRequest(SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
          HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
          HttpStatus.BAD_REQUEST);
    }

    //
    registerUser(signUpRequest.getUsername(), "admin");

    // Creating user's account
    User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
        signUpRequest.getEmail(), signUpRequest.getPassword());

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
        .orElseThrow(() -> new AppException("User Role not set."));

    user.setRoles(Collections.singleton(userRole));

    User result = userRepository.save(user);

    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/admin/users/{username}")
        .buildAndExpand(result.getUsername()).toUri();

    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "User registered successfully"));
  }

  @Override
  public ResponseEntity<?> updateInfoFromRequest(long id, UpdateInfoRequest updateInfoRequest) {

    if (userRepository.existsById(id)) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("User", "username", id));

      user.setName(updateInfoRequest.getName());

      if (!updateInfoRequest.getPassword().isEmpty()) {
        user.setPassword(passwordEncoder.encode((updateInfoRequest.getPassword())));
      }

      User result = userRepository.save(user);

      URI location = ServletUriComponentsBuilder
          .fromCurrentContextPath().path("/api/admin/users/{username}")
          .buildAndExpand(result.getUsername()).toUri();

      return ResponseEntity.created(location)
          .body(new ApiResponse(true, "User updated successfully"));
    } else {
      return new ResponseEntity(new ApiResponse(false, "ID is null!"),
          HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity<?> updateRolesFromRequest(long id, UpdateRolesRequest updateRolesRequest) {

    if (userRepository.existsById(id)) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("User", "username", id));

      String newRole = updateRolesRequest.getRole();

      Role userRole = new Role();

      if (newRole.equals("ROLE_ADMIN")) {
        userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
            .orElseThrow(() -> new AppException("User Role not set."));
      } else if (newRole.equals("ROLE_USER")) {
        userRole = roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new AppException("User Role not set."));
      } else if (newRole.equals("ROLE_SHAREHOLDER_COMMON")) {
        userRole = roleRepository.findByName(RoleName.ROLE_SHAREHOLDER_COMMON)
            .orElseThrow(() -> new AppException("User Role not set."));
      } else if (newRole.equals("ROLE_SHAREHOLDER_FOUNDER")) {
        userRole = roleRepository.findByName(RoleName.ROLE_SHAREHOLDER_FOUNDER)
            .orElseThrow(() -> new AppException("User Role not set."));
      } else if (newRole.equals("ROLE_SHAREHOLDER_PREFERRED")) {
        userRole = roleRepository.findByName(RoleName.ROLE_SHAREHOLDER_PREFERRED)
            .orElseThrow(() -> new AppException("User Role not set."));
      }

      Set<Role> nowRole = user.getRoles();

      nowRole.clear();

      nowRole.add(userRole);

      user.setRoles(nowRole);

      User result = userRepository.save(user);

      URI location = ServletUriComponentsBuilder
          .fromCurrentContextPath().path("/api/admin/users/{username}")
          .buildAndExpand(result.getUsername()).toUri();

      return ResponseEntity.created(location)
          .body(new ApiResponse(true, "User Role updated  successfully"));


    } else {
      return new ResponseEntity(new ApiResponse(false, "ID is null!"),
          HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity<?> deleteUser(long id) {
    try {
      userRepository.deleteById(id);
      return new ResponseEntity(new ApiResponse(true, "Deleted"),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(new ApiResponse(false, "ID is null!"),
          HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }


}
