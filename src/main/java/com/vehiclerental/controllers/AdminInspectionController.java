// src/main/java/com/vehiclerental/controllers/AdminInspectionController.java
package com.vehiclerental.controllers;

import com.vehiclerental.inspection.VehicleInspection;
import com.vehiclerental.inspection.VehicleInspectionRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/inspections")
public class AdminInspectionController {

    private final VehicleInspectionRepository repo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inspections", repo.findAllByOrderByCreatedAtDesc());
        return "admin/inspections_list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        VehicleInspection vi = repo.findById(id)
                .orElseThrow(() -> new ValidationException("Inspection not found"));
        model.addAttribute("i", vi);
        return "admin/inspection_detail";
    }
}
