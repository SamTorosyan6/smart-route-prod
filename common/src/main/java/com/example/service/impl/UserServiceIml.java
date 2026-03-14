package com.example.service.impl;

import com.example.costants.ModelAttributesConstants;
import com.example.costants.ViewName;
import com.example.model.Role;
import com.example.model.UserStatus;
import com.example.model.entitiy.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.utility.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
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
    public String loadDrivers(ModelMap modelMap, int page, int size) {
        Page<User> drivers = findAllByRole(Role.DRIVER, page, size);
        List<Integer> pageNumbers = PaginationUtil.getPageNumbers(drivers.getTotalPages());
        modelMap.addAttribute(ModelAttributesConstants.PAGE_NUMBERS, pageNumbers);
        modelMap.addAttribute(ModelAttributesConstants.DRIVERS, drivers);
        modelMap.addAttribute(ModelAttributesConstants.USER_STATUS, UserStatus.values());
        return ViewName.ADMIN_DRIVERS;
    }

    @Override
    public String loadPassengers(ModelMap modelMap, int page, int size) {
        Page<User> passengers = findAllByRole(Role.PASSENGER, page, size);
        List<Integer> pageNumbers = PaginationUtil.getPageNumbers(passengers.getTotalPages());
        modelMap.addAttribute(ModelAttributesConstants.PAGE_NUMBERS, pageNumbers);
        modelMap.addAttribute(ModelAttributesConstants.PASSENGERS, passengers);
        return ViewName.ADMIN_PASSENGERS;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
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


    private Page<User> findAllByRole(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAllByRole(role, pageable);
    }
}
