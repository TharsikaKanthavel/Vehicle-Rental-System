package com.vehiclerental.controllers;

import com.vehiclerental.services.RentalService;
import com.vehiclerental.services.UserService;
import com.vehiclerental.services.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;

    public AdminController(UserService userService, VehicleService vehicleService, RentalService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("activeRentals", rentalService.getActiveRentalsCount());
        model.addAttribute("availableVehicles", vehicleService.getAvailableVehiclesCount());
        model.addAttribute("totalRevenue", rentalService.getTotalRevenue());
        model.addAttribute("recentUsers",
                userService.getAllUsers().subList(0, Math.min(5, userService.getAllUsers().size())));
        return "admin/dashboard";
    }
}
