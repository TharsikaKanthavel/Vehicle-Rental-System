package com.vehiclerental.controllers.admin;

import com.vehiclerental.models.Vehicle;
import com.vehiclerental.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {

    private final VehicleRepository vehicleRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "admin/vehicles/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("vehicle", new Vehicle());   // <-- provide blank bean
        return "admin/vehicles/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        model.addAttribute("vehicle", v);               // <-- provide the entity
        return "admin/vehicles/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        return "redirect:/admin/vehicles";
    }
}
