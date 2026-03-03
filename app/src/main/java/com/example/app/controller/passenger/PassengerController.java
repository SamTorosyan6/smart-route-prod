package com.example.app.controller.passenger;

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
public class PassengerController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;

    @GetMapping("passenger/home")
    public String passengerHome(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if(springUser != null){
            modelMap.addAttribute("user",springUser.getUser() );
        }
        return "passengerPackage/passengerHome";
    }

    @GetMapping("/register/passenger")
    public String registerPassenger(@RequestParam(required = false) String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg",msg);
        return "passengerPackage/registerPassenger";
    }

    @PostMapping("/register/passenger")
    public String registerPassenger(@ModelAttribute User user){
        if(userService.findByEmail(user.getEmail()).isPresent()){
            return "redirect:/register/passenger?msg=Email already in exists!";
        }
        user.setRole(Role.PASSENGER);
        user.setStatus(UserStatus.ACCEPTED);
        user.setBlocked(false);
        user.setRatingAverage(BigDecimal.ZERO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegion(regionRepository.findById(1).orElseThrow(()
                ->new RuntimeException("Region not found!")));

        userService.save(user);
        return "redirect:/loginPage?msg=Registration Successful!";
    }
}
