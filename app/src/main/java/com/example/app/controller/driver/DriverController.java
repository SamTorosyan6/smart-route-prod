package com.example.app.controller.driver;

import com.example.app.service.security.SpringUser;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserStatus;
import com.example.repository.RegionRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;


@Controller
@RequiredArgsConstructor
public class DriverController {
    private  final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;

    @GetMapping("driver/home")
    public String driverHome(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if(springUser != null){
            modelMap.addAttribute("user",springUser.getUser() );
        }
        return "driverPackage/driverHome";
    }

    @GetMapping("/register/driver")
    public String registerDriver(@RequestParam(required = false)String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg",msg);
        return "driverPackage/registerDriver";
    }

    @PostMapping("/register/driver")
    public String registerDriver(@ModelAttribute User user){
        if(userService.findByEmail(user.getEmail()).isPresent()){
            return "redirect:/register/driver?msg=Email already in exists!";
        }
        user.setRole(Role.DRIVER);
        user.setBlocked(false);
        user.setRatingAverage(BigDecimal.ZERO);
        user.setStatus(UserStatus.ACCEPTED);
        user.setRegion(regionRepository.findById(1).orElseThrow(()
                ->new RuntimeException("Region not found!")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/loginPage?msg=Registration Successful!";
    }
}
