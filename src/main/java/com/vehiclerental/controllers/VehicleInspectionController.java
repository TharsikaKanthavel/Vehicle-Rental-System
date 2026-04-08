package com.vehiclerental.controllers;

import com.vehiclerental.inspection.*;
import com.vehiclerental.inspection.dto.InspectionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class VehicleInspectionController {

    private final VehicleInspectionService service;

    @GetMapping("/driver/inspections/new")
    public String newInspection(@RequestParam Long deliveryId,
                                @RequestParam Long vehicleId,
                                Model model) {
        InspectionRequest req = new InspectionRequest();
        req.deliveryId = deliveryId;
        req.vehicleId = vehicleId;
        model.addAttribute("inspection", req);
        model.addAttribute("fuelLevels", FuelLevel.values());
        model.addAttribute("cleanlinessValues", Cleanliness.values());
        return "driver/inspection_form";
    }

    @PostMapping("/driver/inspections")
    public String create(Authentication auth,
                         @Valid @ModelAttribute("inspection") InspectionRequest req,
                         BindingResult br,
                         @RequestParam("photoFront") MultipartFile front,
                         @RequestParam("photoRear") MultipartFile rear,
                         @RequestParam("photoLeft") MultipartFile left,
                         @RequestParam("photoRight") MultipartFile right,
                         @RequestParam("photoInterior") MultipartFile interior,
                         @RequestParam("photoDashboard") MultipartFile dashboard,
                         Model model) {
        if (br.hasErrors()) {
            model.addAttribute("fuelLevels", FuelLevel.values());
            model.addAttribute("cleanlinessValues", Cleanliness.values());
            return "driver/inspection_form";
        }
        Long inspectorId = currentUserId(auth); // replace with your actual user-id getter
        service.create(inspectorId, req, front, rear, left, right, interior, dashboard);
        return "redirect:/driver/deliveries";
    }

    private Long currentUserId(Authentication auth) {
        // Adapt this to your UserDetails. Placeholder:
        return Long.valueOf(0);
    }
}
