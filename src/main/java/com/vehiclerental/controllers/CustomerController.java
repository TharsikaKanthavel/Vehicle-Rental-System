package com.vehiclerental.controllers;

import com.vehiclerental.models.User;
import com.vehiclerental.services.UserService;
import com.vehiclerental.services.VehicleService;
import com.vehiclerental.services.RentalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;

    public CustomerController(UserService userService, VehicleService vehicleService, RentalService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    @GetMapping("/dashboard")
    public String customerDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("activeRentals", rentalService.getUserActiveRentals(user));
        model.addAttribute("rentalHistory", rentalService.getUserRentalHistory(user));
        model.addAttribute("availableVehicles", vehicleService.getAvailableVehicles());

        return "customer/dashboard";
    }

    @GetMapping("/profile")
    public String customerProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "customer/profile";
    }

    @GetMapping("/rentals")
    public String customerRentals(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("rentals", rentalService.getUserRentals(user));
        return "customer/rentals";
    }
}