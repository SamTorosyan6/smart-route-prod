package com.example.repository;

import com.example.model.Role;
import com.example.model.entitiy.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
