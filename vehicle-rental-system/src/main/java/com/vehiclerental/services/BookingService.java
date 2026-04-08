package com.vehiclerental.services;

import com.vehiclerental.models.Booking;
import com.vehiclerental.models.BookingStatus;
import com.vehiclerental.models.DriverTripStatus;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking createBooking(Booking b);

    boolean isVehicleAvailable(Long vehicleId, LocalDateTime pickup, LocalDateTime ret);

    Optional<Booking> findById(Long id);

    List<Booking> findByCustomer(Long customerId);

    List<Booking> findPendingApprovals();

    List<Booking> findAll();

    Booking updateStatus(Long bookingId, BookingStatus status);

    Booking save(Booking b);

    /** NEW: assignments for a driver, filtered by trip statuses (and only CONFIRMED bookings). */
    List<Booking> findDriverAssignments(Long driverId, EnumSet<DriverTripStatus> tripStatuses);
}
