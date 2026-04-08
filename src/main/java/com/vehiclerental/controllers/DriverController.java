package com.vehiclerental.controllers;

import com.vehiclerental.models.User;
import com.vehiclerental.services.UserService;
import com.vehiclerental.services.RentalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/driver")
public class DriverController {

    private final UserService userService;
    private final RentalService rentalService;

    public DriverController(UserService userService, RentalService rentalService) {
        this.userService = userService;
        this.rentalService = rentalService;
    }

    @GetMapping("/dashboard")
    public String driverDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("assignedRentals", rentalService.getDriverAssignedRentals(user));
        model.addAttribute("completedRentals", rentalService.getDriverCompletedRentals(user));

        return "driver/dashboard";
    }

    @GetMapping("/schedule")
    public String driverSchedule(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("schedule", rentalService.getDriverSchedule(user));
        return "driver/schedule";
    }
}