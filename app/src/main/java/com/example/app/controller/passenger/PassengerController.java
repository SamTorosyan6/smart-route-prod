package com.example.app.controller.passenger;

import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserStatus;
import com.example.repository.RegionRepository;
import com.example.service.UserService;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final RegionRepository regionRepository;

    @GetMapping("passenger/home")
    public String passengerHome(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if(springUser != null){
            modelMap.addAttribute("user",springUser.getUser() );
        }
        return "passengerPackage/passengerHome";
    }

    @GetMapping("/passenger/register")
    public String registerPassenger(@RequestParam(required = false) String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg",msg);
        modelMap.addAttribute("regions",regionRepository.findAll());
        return "passengerPackage/registerPassenger";
    }

    @PostMapping("/passenger/register")
    public String registerPassenger(@ModelAttribute User user,
                                    @RequestParam Integer regionId){
        if(userService.findByEmail(user.getEmail()).isPresent()){
            return "redirect:/passenger/register?msg=Email already in exists!";
        }
        user.setRole(Role.PASSENGER);
        user.setStatus(UserStatus.ACCEPTED);
        user.setBlocked(false);
        user.setRatingAverage(BigDecimal.ZERO);
        user.setRegion(regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found!")));

        userService.save(user);
        return "redirect:/loginPage?msg=Registration Successful!";
    }
}
