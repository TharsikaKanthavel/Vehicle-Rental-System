package com.vehiclerental.controllers;

import com.vehiclerental.services.RentalService;
import com.vehiclerental.services.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/finance")
public class FinanceController {

    private final RentalService rentalService;
    private final VehicleService vehicleService;

    public FinanceController(RentalService rentalService, VehicleService vehicleService) {
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/dashboard")
    public String financeDashboard(Model model) {
        model.addAttribute("totalRevenue", rentalService.getTotalRevenue());
        model.addAttribute("monthlyRevenue", rentalService.getMonthlyRevenue());
        model.addAttribute("pendingPayments", rentalService.getPendingPayments());
        model.addAttribute("revenueChartData", rentalService.getRevenueChartData());
        return "finance/dashboard";
    }

    @GetMapping("/reports")
    public String financialReports(Model model) {
        model.addAttribute("revenueReport", rentalService.getRevenueReport());
        model.addAttribute("vehiclePerformance", vehicleService.getVehiclePerformanceReport());
        return "finance/reports";
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        model.addAttribute("transactions", rentalService.getAllTransactions());
        return "finance/transactions";
    }
}