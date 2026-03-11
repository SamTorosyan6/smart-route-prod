package com.example.service.impl;

import com.example.model.Role;
import com.example.model.UserStatus;
import com.example.model.entitiy.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public Page<User> findAllByRole(Role role, Pageable pageable) {
        return userRepository.findAllByRole(role, pageable);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public void toggleBlocked(Integer id) {
        User user = findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
    }

    @Override
    public void changeUserStatus(Integer id, UserStatus userStatus) {
       User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found"));
       user.setStatus(userStatus);
       userRepository.save(user);


    }

    @Override
    public boolean deletesUserById(Integer id) {
        userRepository.deleteById(id);
        return true;
    }
}
