package com.example.fabcarapplication.repository;

import com.example.fabcarapplication.model.user.User;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

  List<User> findByIdIn(List<Long> userIds);

  List<User> findAll();

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

}