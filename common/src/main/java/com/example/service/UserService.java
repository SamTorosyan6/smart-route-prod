package com.example.service;

import com.example.model.Role;
import com.example.model.UserStatus;
import com.example.model.entitiy.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByEmail(String email);

    Page<User> findAllByRole(Role role, int currentPage, int pageSize);

    Optional<User> findById(Integer id);

    void toggleBlocked(Integer id);

    void changeUserStatus(Integer id, UserStatus userStatus);

    boolean deletesUserById(Integer id);

}
