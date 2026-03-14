package com.example.service;

import com.example.model.UserStatus;
import com.example.model.entitiy.User;
import org.springframework.ui.ModelMap;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByEmail(String email);

    String loadDrivers(ModelMap modelMap,int page,int size);

    String loadPassengers(ModelMap modelMap,int page,int size);

    Optional<User> findById(Integer id);

    void toggleBlocked(Integer id);

    void changeUserStatus(Integer id, UserStatus userStatus);

    boolean deletesUserById(Integer id);

}
