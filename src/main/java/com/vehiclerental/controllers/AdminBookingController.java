package com.vehiclerental.controllers;

import com.vehiclerental.models.*;
import com.vehiclerental.repositories.PaymentRepository;
import com.vehiclerental.services.BookingService;
import com.vehiclerental.services.DriverService;
import com.vehiclerental.services.InvoiceService;
import com.vehiclerental.services.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/bookings")
public class AdminBookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final DriverService driverService;

    public AdminBookingController(BookingService bookingService,
                                  PaymentService paymentService,
                                  PaymentRepository paymentRepository,
                                  InvoiceService invoiceService,
                                  DriverService driverService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
        this.driverService = driverService;
    }

    @GetMapping("/pending")
    public String pending(Model model) {
        List<Booking> pending = bookingService.findPendingApprovals();
        model.addAttribute("pending", pending);
        model.addAttribute("drivers", driverService.listDrivers()); // for dropdown
        return "admin/bookings_pending";
    }

    @PostMapping("/{id}/assign")
    public String assignDriver(@PathVariable Long id,
                               @RequestParam Long driverId,
                               @RequestParam(required = false) String note) {
        Booking booking = bookingService.findById(id).orElseThrow();
        booking.setAssignedDriverId(driverId);
        booking.setDriverNote(note);
        booking.setDriverTripStatus(DriverTripStatus.ASSIGNED);
        bookingService.save(booking);
        return "redirect:/admin/bookings/pending";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findById(id).orElseThrow();
        if (booking.getPaymentId() == null) {
            model.addAttribute("error", "No payment pre-authorization found for this booking.");
            return "redirect:/admin/bookings/pending";
        }
        var p = paymentRepository.findById(booking.getPaymentId()).orElseThrow();
        paymentService.capture(p.getId());
        p = paymentRepository.findById(p.getId()).orElseThrow();

        if (p.getStatus() == PaymentStatus.CAPTURED) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingService.save(booking);
            invoiceService.generateInvoiceForBooking(id);
        } else {
            model.addAttribute("error", "Payment capture failed; booking left pending.");
        }
        return "redirect:/admin/bookings/pending";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        Booking booking = bookingService.findById(id).orElseThrow();
        if (booking.getPaymentId() != null) {
            paymentService.refund(booking.getPaymentId());
        }
        bookingService.updateStatus(id, BookingStatus.REJECTED);
        return "redirect:/admin/bookings/pending";
    }
}
