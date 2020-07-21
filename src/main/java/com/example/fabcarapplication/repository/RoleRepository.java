package com.example.fabcarapplication.repository;

import com.example.fabcarapplication.model.user.Role;
import com.example.fabcarapplication.model.user.RoleName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleName roleName);


}