package com.example.service;

import com.example.model.User;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByEmail(String email);

}
