package com.vaggelis.UserManagementSystem.repositories;

import com.vaggelis.UserManagementSystem.models.Role;
import com.vaggelis.UserManagementSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);

    User findUserByEmail(String email);
    User findUserById(Long id);
}
