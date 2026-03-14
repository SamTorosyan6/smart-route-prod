package com.example.app.controller;

import com.example.service.impl.ImageService;
import com.example.service.impl.LoginRedirectService;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final LoginRedirectService loginRedirectService;
    private final ImageService imageService;

    @Value("${app.upload.license-path}")
    private String licenseUploadPath;

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
    public String login(@RequestParam(required = false) String msg,
                        @RequestParam(required = false) String error,
                        ModelMap modelMap){
        modelMap.addAttribute("error", error);
        modelMap.addAttribute("msg", msg);
        return "loginPage";
    }
    @GetMapping("/registerPage")
    public String registerPage() {
        return "registerPage";
    }

    @GetMapping("/image/get")
    public ResponseEntity<byte[]> getImage(@RequestParam("photo") String licensePhoto) {
        return imageService.getImage(licenseUploadPath, licensePhoto);
    }


}
