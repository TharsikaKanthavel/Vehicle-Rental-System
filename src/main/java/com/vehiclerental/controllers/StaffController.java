package com.vehiclerental.controllers;

import com.vehiclerental.services.VehicleService;
import com.vehiclerental.services.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final VehicleService vehicleService;
    private final RentalService rentalService;

    public StaffController(VehicleService vehicleService, RentalService rentalService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    @GetMapping("/dashboard")
    public String staffDashboard(Model model) {
        model.addAttribute("pendingRentals", rentalService.getPendingRentals());
        model.addAttribute("vehiclesNeedingMaintenance", vehicleService.getVehiclesNeedingMaintenance());
        model.addAttribute("availableVehicles", vehicleService.getAvailableVehiclesCount());
        return "staff/dashboard";
    }

    @GetMapping("/rentals")
    public String manageRentals(Model model) {
        model.addAttribute("rentals", rentalService.getAllRentals());
        return "staff/rentals";
    }

    @GetMapping("/vehicles")
    public String manageVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "staff/vehicles";
    }
}