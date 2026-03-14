package com.example.app.controller.admin;


import com.example.costants.ModelAttributesConstants;
import com.example.costants.PaginationConstants;
import com.example.costants.ViewName;
import com.example.model.UserStatus;
import com.example.service.UserService;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("admin/home")
    public String adminHome(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute(ModelAttributesConstants.SPRING_USER, springUser.getUser());
        return ViewName.ADMIN_HOME;
    }

    @GetMapping("/admin/drivers")
    public String showDrivers(ModelMap modelMap,
                              @RequestParam(defaultValue = PaginationConstants.CURRENT_PAGE_SIZE) int page,
                              @RequestParam(defaultValue = PaginationConstants.PAGE_SIZE) int size) {

        return userService.loadDrivers(modelMap, page, size) ;
    }

    @GetMapping("/admin/passengers")
    public String showPassengers(ModelMap modelMap,
                                 @RequestParam(defaultValue = PaginationConstants.CURRENT_PAGE_SIZE) int page,
                                 @RequestParam(defaultValue = PaginationConstants.PAGE_SIZE) int size) {

        return userService.loadPassengers(modelMap, page, size) ;
    }

    @PostMapping("/admin/users/{id}/block")
    public String blockUser(@PathVariable Integer id, @RequestHeader("referer") String referer) {
        userService.toggleBlocked(id);
        return "redirect:" + referer;
    }

    @PostMapping("/admin/changeUserStatus")
    public String changeUserStatus(@RequestParam Integer id,
                                   @RequestParam UserStatus userStatus,
                                   @RequestHeader("referer") String referer) {
        userService.changeUserStatus(id, userStatus);
        return "redirect:" + referer;
    }
}
