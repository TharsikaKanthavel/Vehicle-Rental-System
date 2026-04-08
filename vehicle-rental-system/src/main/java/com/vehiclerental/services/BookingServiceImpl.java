package com.vehiclerental.services;

import com.vehiclerental.models.Booking;
import com.vehiclerental.models.BookingStatus;
import com.vehiclerental.models.DriverTripStatus;
import com.vehiclerental.repositories.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Booking b) {
        if (b.getPickupDateTime() == null || b.getReturnDateTime() == null)
            throw new IllegalArgumentException("Pickup and return datetime required");

        if (!b.getReturnDateTime().isAfter(b.getPickupDateTime()))
            throw new IllegalArgumentException("Return must be after pickup");

        if (b.getPickupDateTime().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Pickup must be in the future");

        // prevent overlap only with already confirmed bookings
        if (!isVehicleAvailable(b.getVehicleId(), b.getPickupDateTime(), b.getReturnDateTime()))
            throw new IllegalArgumentException("Vehicle not available for chosen period");

        long seconds = Duration.between(b.getPickupDateTime(), b.getReturnDateTime()).getSeconds();
        long days = (seconds + 86399) / 86400; // ceil
        b.setEstimatedTotal(days * b.getBasePricePerDay());

        b.setStatus(BookingStatus.PENDING_APPROVAL);
        return bookingRepository.save(b);
    }

    @Override
    public boolean isVehicleAvailable(Long vehicleId, LocalDateTime pickup, LocalDateTime ret) {
        return bookingRepository.findOverlappingConfirmed(vehicleId, pickup, ret).isEmpty();
    }

    @Override public Optional<Booking> findById(Long id) { return bookingRepository.findById(id); }

    @Override public List<Booking> findByCustomer(Long customerId) {
        return bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public List<Booking> findPendingApprovals() {
        return bookingRepository.findByStatusOrderByCreatedAtDesc(BookingStatus.PENDING_APPROVAL);
    }

    @Override public List<Booking> findAll() { return bookingRepository.findAll(); }

    @Override
    public Booking updateStatus(Long bookingId, BookingStatus status) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        b.setStatus(status);
        return bookingRepository.save(b);
    }

    @Override public Booking save(Booking b) { return bookingRepository.save(b); }

    @Override
    public List<Booking> findDriverAssignments(Long driverId, EnumSet<DriverTripStatus> tripStatuses) {
        // Only CONFIRMED bookings are visible to drivers
        return bookingRepository
                .findByAssignedDriverIdAndStatusAndDriverTripStatusInOrderByPickupDateTimeAsc(
                        driverId, BookingStatus.CONFIRMED, tripStatuses);
    }
}
