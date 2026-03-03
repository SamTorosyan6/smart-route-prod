package com.example.app.controller;

import com.example.app.service.security.SpringUser;
import com.example.model.Role;
import com.example.model.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if(springUser != null) {
            modelMap.addAttribute("user", springUser.getUser());
        }
        return "home";
    }

    @GetMapping("/successLogin")
    public String successLogin(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser == null || springUser.getUser().getStatus() != UserStatus.ACCEPTED) {
            return "redirect:/login?error=account_not_active";
        }

        Role role = springUser.getUser().getRole();

        switch(role) {
            case ADMIN:
                return "redirect:/admin/home";
            case DRIVER:
                return "redirect:/driver/home";
            case PASSENGER:
                return "redirect:/passenger/home";
            default:
                return "redirect:/";
        }
    }

    @GetMapping("/loginPage")
    public String login(@RequestParam(required = false) String msg, ModelMap modelMap){
        modelMap.addAttribute("msg", msg);
        return "loginPage";
    }
    @GetMapping("/registerPage")
    public String registerPage() {
        return "registerPage";
    }
}
