package com.vehiclerental.controllers;

import com.vehiclerental.models.DriverTripStatus;
import com.vehiclerental.services.BookingService;
import com.vehiclerental.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;

@Controller
@RequestMapping("/driver")
public class DriverAssignmentsController {

    private final BookingService bookingService;
    private final UserService userService;

    public DriverAssignmentsController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/assignments")
    public String myAssignments(Authentication auth, Model model) {
        var me = userService.findByUsername(auth.getName());

        // Show active driver-trip states (adjust the set to what your enum actually contains)
        var activeStates = EnumSet.of(DriverTripStatus.ASSIGNED);

        var myTrips = bookingService.findDriverAssignments(me.getId(), activeStates);
        model.addAttribute("trips", myTrips);
        model.addAttribute("statuses", DriverTripStatus.values());
        return "driver/assignments";
    }

    @PostMapping("/assignments/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam DriverTripStatus status,
                               Authentication auth) {
        var me = userService.findByUsername(auth.getName());
        var b = bookingService.findById(id).orElseThrow();
        if (!me.getId().equals(b.getAssignedDriverId())) {
            return "redirect:/driver/assignments";
        }
        b.setDriverTripStatus(status);
        bookingService.save(b);
        return "redirect:/driver/assignments";
    }
}
