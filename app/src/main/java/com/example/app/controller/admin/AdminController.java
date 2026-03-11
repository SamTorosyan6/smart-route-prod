package com.example.app.controller.admin;


import com.example.model.Role;
import com.example.model.UserStatus;
import com.example.model.entitiy.User;
import com.example.service.UserService;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("admin/home")
    public String adminHome(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        modelMap.addAttribute("user", springUser.getUser());
        return "adminPackage/adminHome";
    }

    @GetMapping("/admin/drivers")
    public String showDrivers(ModelMap modelMap,
                            @RequestParam(required = false)Optional<Integer> page,
                            @RequestParam(required = false)Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage -1, pageSize);
           Page<User> drivers;
           drivers = userService.findAllByRole(Role.DRIVER,pageable);

           int totalPages = drivers.getTotalPages();
           if (totalPages > 0) {
               List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                       .boxed().toList();
               modelMap.addAttribute("pageNumbers", pageNumbers);
           }

           modelMap.addAttribute("drivers", drivers);
           modelMap.addAttribute("userStatus", UserStatus.values());

        return "adminPackage/adminDrivers";
    }

    @GetMapping("/admin/passengers")
    public String showPassengers(ModelMap modelMap,
                            @RequestParam(required = false)Optional<Integer> page,
                            @RequestParam(required = false)Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage -1, pageSize);
           Page<User> passengers;
           passengers = userService.findAllByRole(Role.PASSENGER,pageable);

           int totalPages = passengers.getTotalPages();
           if (totalPages > 0) {
               List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                       .boxed().toList();
               modelMap.addAttribute("pageNumbers", pageNumbers);
           }

           modelMap.addAttribute("passengers", passengers);

        return "adminPackage/adminPassengers";
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
