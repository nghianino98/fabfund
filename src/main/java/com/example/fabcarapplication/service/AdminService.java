package com.example.fabcarapplication.service;

import com.example.fabcarapplication.model.user.Role;
import com.example.fabcarapplication.payload.request.SignUpRequest;
import com.example.fabcarapplication.payload.UserProfile;
import com.example.fabcarapplication.payload.request.UpdateInfoRequest;
import com.example.fabcarapplication.payload.request.UpdateRolesRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  boolean enrollAdmin();

  boolean registerUser(String userName, String adminName);

  UserProfile getUserProfileByUsername(String username);

  UserProfile getUserProfileById(long id);

  List<UserProfile> getAllUserProfiles(int roleId);

  List<String> getAllActorId();

  List<UserProfile> getAllShareholderProfiles();

  List<UserProfile> getAllUserProfilesByRole(int idRole);

  ResponseEntity<?> addUserFromRequest(SignUpRequest signUpRequest);

  ResponseEntity<?> updateInfoFromRequest(long id, UpdateInfoRequest updateInfoRequest);

  ResponseEntity<?> updateRolesFromRequest(long id, UpdateRolesRequest updateRolesRequest);

  ResponseEntity<?> deleteUser(long id);

  List<Role> getAllRoles();

}
