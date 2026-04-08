package com.vehiclerental.controllers.manager;

import com.vehiclerental.repositories.MaintenanceRepository;
import com.vehiclerental.repositories.RentalRepository;
import com.vehiclerental.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/vehicles")
public class VehicleMgmtPageController {

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final MaintenanceRepository maintenanceRepository;

    @GetMapping
    public String list() { return "manager/vehicles/list"; }

    @GetMapping("/new")
    public String create(Model m) { m.addAttribute("mode","create"); return "manager/vehicles/form"; }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model m) {
        m.addAttribute("mode","edit");
        m.addAttribute("vehicleId", id);
        return "manager/vehicles/form";
    }

    // Helper landing to pick vehicle then view calendar
    @GetMapping("/availability")
    public String availability(Model m) {
        m.addAttribute("vehicles", vehicleRepository.findAll());
        return "manager/vehicles/availability";
    }

    @GetMapping("/{id}/calendar")
    public String calendar(@PathVariable Long id,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                           Model m) {
        m.addAttribute("vehicleId", id);
        m.addAttribute("vehicle", vehicleRepository.findById(id).orElseThrow());
        m.addAttribute("from", from);
        m.addAttribute("to", to);
        return "manager/vehicles/calendar";
    }
}
