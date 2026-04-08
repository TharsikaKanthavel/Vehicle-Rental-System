package com.vehiclerental.controllers;

import com.vehiclerental.dto.BookingRequestDto;
import com.vehiclerental.models.Booking;
import com.vehiclerental.models.BookingStatus;
import com.vehiclerental.models.User;
import com.vehiclerental.models.Vehicle;
import com.vehiclerental.services.BookingService;
import com.vehiclerental.services.PaymentService;
import com.vehiclerental.services.UserService;
import com.vehiclerental.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer/bookings")
public class CustomerBookingController {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public CustomerBookingController(UserService userService,
                                     VehicleService vehicleService,
                                     BookingService bookingService,
                                     PaymentService paymentService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    @GetMapping("/new")
    public String newBooking(@RequestParam("vehicleId") Long vehicleId,
                             Model model,
                             RedirectAttributes ra) {

        BookingRequestDto dto = new BookingRequestDto();
        dto.setVehicleId(vehicleId);

        Optional<Vehicle> vOpt = vehicleService.findById(vehicleId);
        if (vOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Selected vehicle was not found.");
            return "redirect:/customer/vehicles";
        }

        Vehicle v = vOpt.get();
        BigDecimal rate = v.getDailyRate();
        if (rate != null) {
            dto.setBasePricePerDay(rate.doubleValue());
        } else {
            dto.setBasePricePerDay(0.0);
            model.addAttribute("warn", "Price per day is not set for this vehicle. Please verify before reserving.");
        }

        model.addAttribute("bookingRequest", dto);
        return "customer/booking_new";
    }

    @PostMapping
    public String createBooking(@ModelAttribute("bookingRequest") @Valid BookingRequestDto bookingRequest,
                                BindingResult br,
                                Authentication authentication,
                                Model model) {
        if (br.hasErrors()) {
            model.addAttribute("errors", br.getAllErrors());
            return "customer/booking_new";
        }

        User current = userService.findByUsername(authentication.getName());

        Booking booking = new Booking();
        booking.setCustomerId(current.getId());
        booking.setVehicleId(bookingRequest.getVehicleId());
        booking.setPickupDateTime(bookingRequest.getPickupDateTime());
        booking.setReturnDateTime(bookingRequest.getReturnDateTime());
        booking.setPickupLocation(bookingRequest.getPickupLocation());
        booking.setDropoffLocation(bookingRequest.getDropoffLocation());

        // authoritative price from vehicle if present; fallback to DTO
        double pricePerDay = bookingRequest.getBasePricePerDay();
        try {
            var vOpt = vehicleService.findById(bookingRequest.getVehicleId());
            if (vOpt.isPresent() && vOpt.get().getDailyRate() != null) {
                pricePerDay = vOpt.get().getDailyRate().doubleValue();
            }
        } catch (Exception ignored) {}
        booking.setBasePricePerDay(pricePerDay);

        try {
            Booking saved = bookingService.createBooking(booking);
            return "redirect:/customer/bookings/checkout?bookingId=" + saved.getId();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "customer/booking_new";
        }
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("bookingId") Long bookingId,
                           Authentication auth,
                           Model model) {
        User current = userService.findByUsername(auth.getName());
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!booking.getCustomerId().equals(current.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your booking");
        }
        model.addAttribute("booking", booking);
        return "customer/checkout";
    }

    @PostMapping("/pay")
    public String pay(@RequestParam Long bookingId,
                      @RequestParam String method,
                      Authentication auth,
                      Model model) {
        User current = userService.findByUsername(auth.getName());
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!booking.getCustomerId().equals(current.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your booking");
        }

        var payment = paymentService.initiatePreAuth(bookingId, booking.getEstimatedTotal(), method);
        booking.setPaymentId(payment.getId());
        bookingService.updateStatus(bookingId, BookingStatus.PENDING_APPROVAL);

        model.addAttribute("message", "Pre-authorization successful. Admin will confirm your booking and capture payment.");
        return "redirect:/customer/bookings/my";
    }

    @GetMapping("/my")
    public String myBookings(Authentication auth, Model model) {
        User current = userService.findByUsername(auth.getName());
        List<Booking> list = bookingService.findByCustomer(current.getId());
        model.addAttribute("bookings", list);
        return "customer/bookings";
    }
}
