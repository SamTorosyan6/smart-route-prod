package com.example.app.controller;

import com.example.service.impl.LoginRedirectService;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final LoginRedirectService loginRedirectService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if(springUser != null) {
            modelMap.addAttribute("user", springUser.getUser());
        }
        return "home";
    }

    @GetMapping("/successLogin")
    public String successLogin(@AuthenticationPrincipal SpringUser springUser) {
        return loginRedirectService.getRedirectUrl(springUser);
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
