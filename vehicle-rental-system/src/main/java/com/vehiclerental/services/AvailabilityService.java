package com.vehiclerental.services;

import com.vehiclerental.models.Rental;
import com.vehiclerental.repositories.MaintenanceRepository;
import com.vehiclerental.repositories.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final RentalRepository rentalRepository;
    private final MaintenanceRepository maintenanceRepository;

    public boolean isAvailable(Long vehicleId, LocalDate from, LocalDate to) {
        if (from == null || to == null || !from.isBefore(to)) return false;

        // We use LocalDate in repos; no LocalDateTime conversions
        var hasBooking = !rentalRepository.findOverlaps(vehicleId, from, to).isEmpty();
        if (hasBooking) return false;

        var hasMaint = !maintenanceRepository.findOverlaps(vehicleId, from, to).isEmpty();
        return !hasMaint;
    }

    public String firstBlockReason(Long vehicleId, LocalDate from, LocalDate to) {
        if (!rentalRepository.findOverlaps(vehicleId, from, to).isEmpty()) return "booked";
        if (!maintenanceRepository.findOverlaps(vehicleId, from, to).isEmpty()) return "maintenance";
        return "available";
    }

    public boolean hasActiveOrFuture(Long vehicleId) {
        return rentalRepository.existsByVehicleIdAndEndDateGreaterThanEqualAndStatus(
                vehicleId,
                LocalDate.now(),
                Rental.RentalStatus.ACTIVE
        );
    }
}
