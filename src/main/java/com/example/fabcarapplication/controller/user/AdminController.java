package com.example.fabcarapplication.controller.user;


import com.example.fabcarapplication.model.user.Role;
import com.example.fabcarapplication.payload.UserProfile;
import com.example.fabcarapplication.payload.request.SignUpRequest;
import com.example.fabcarapplication.payload.request.UpdateInfoRequest;
import com.example.fabcarapplication.payload.request.UpdateRolesRequest;
import com.example.fabcarapplication.service.AdminService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Secured("ROLE_ADMIN")
@RequestMapping(value = "api/admin/users")
public class AdminController {

  @Autowired
  AdminService adminService;

  @Secured("ROLE_ADMIN")
  @GetMapping("/{id}")
  public UserProfile getUserProfileByID(@PathVariable(value = "id") long id) {
    return adminService.getUserProfileById(id);
  }

  @Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON"})
  @GetMapping("")
  public List<UserProfile> getAllUserProfile(@RequestParam(defaultValue = "0") String role) {
    return adminService.getAllUserProfiles(Integer.parseInt(role));
  }

  @Secured({"ROLE_SHAREHOLDER_FOUNDER", "ROLE_ADMIN", "ROLE_SHAREHOLDER_PREFERRED", "ROLE_SHAREHOLDER_COMMON"})
  @GetMapping("/actorId")
  public List<String> getAllActorId() {
    return adminService.getAllActorId();
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("")
  public ResponseEntity<?> addUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    return adminService.addUserFromRequest(signUpRequest);
  }

  @PutMapping("/{id}")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<?> updateInfoUser(@PathVariable(value = "id") long id,
      @Valid @RequestBody UpdateInfoRequest updateInfoRequest) {
    return adminService.updateInfoFromRequest(id, updateInfoRequest);
  }

  @Secured("ROLE_ADMIN")
  @PutMapping("/roles/{id}")
  public ResponseEntity<?> updateRoleUser(@PathVariable(value = "id") long id,
      @Valid @RequestBody UpdateRolesRequest updateRolesRequest) {
    return adminService.updateRolesFromRequest(id, updateRolesRequest);
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable(value = "id") long id) {
    return adminService.deleteUser(id);
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/roles")
  public List<Role> getAllRoles() {
    return adminService.getAllRoles();
  }


}
