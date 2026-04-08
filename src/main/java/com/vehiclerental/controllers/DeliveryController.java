package com.vehiclerental.controllers;

import com.vehiclerental.delivery.*;
import com.vehiclerental.delivery.dto.DeliveryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class DeliveryController {

    private final DeliveryService deliveryService;

    // DRIVER – list own deliveries
    @GetMapping("/driver/deliveries")
    public String driverDeliveries(Authentication auth, Model model) {
        Long driverId = currentUserId(auth); // adapt if your UserDetails differs
        model.addAttribute("deliveries", deliveryService.listForDriver(driverId));
        model.addAttribute("statusValues", DeliveryStatus.values());
        return "driver/deliveries_list";
    }

    // ADMIN – list by status
    @GetMapping("/admin/deliveries")
    public String adminDeliveries(@RequestParam(value="status", required=false) DeliveryStatus status, Model model) {
        model.addAttribute("deliveries", deliveryService.listByStatus(status == null ? DeliveryStatus.SCHEDULED : status));
        model.addAttribute("statusValues", DeliveryStatus.values());
        model.addAttribute("selectedStatus", status);
        return "admin/deliveries_list";
    }

    // ADMIN – create (GET form)
    @GetMapping("/admin/deliveries/new")
    public String newDelivery(Model model) {
        model.addAttribute("delivery", new DeliveryRequest());
        model.addAttribute("statusValues", DeliveryStatus.values());
        return "admin/delivery_form";
    }

    // ADMIN – create (POST)
    @PostMapping("/admin/deliveries")
    public String create(Authentication auth, @Valid @ModelAttribute("delivery") DeliveryRequest req, BindingResult br, Model model) {
        if (br.hasErrors()) return "admin/delivery_form";
        Long schedulerId = currentUserId(auth);
        deliveryService.create(schedulerId, req);
        return "redirect:/admin/deliveries?status=SCHEDULED";
    }

    // DRIVER actions
    @PostMapping("/driver/deliveries/{id}/start")
    public String start(@PathVariable Long id) {
        deliveryService.start(id);
        return "redirect:/driver/deliveries";
    }

    @PostMapping("/driver/deliveries/{id}/complete")
    public String complete(@PathVariable Long id) {
        deliveryService.complete(id);
        return "redirect:/driver/deliveries";
    }

    // ADMIN cancel
    @PostMapping("/admin/deliveries/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        deliveryService.cancel(id);
        return "redirect:/admin/deliveries?status=CANCELLED";
    }

    private Long currentUserId(Authentication auth) {
        // If your UserDetails exposes id, cast and return. Otherwise, adapt this method.
        // Example:
        // CustomUserDetails u = (CustomUserDetails) auth.getPrincipal();
        // return u.getId();
        return Long.valueOf(0); // TODO: replace with your actual user id extraction
    }
}
