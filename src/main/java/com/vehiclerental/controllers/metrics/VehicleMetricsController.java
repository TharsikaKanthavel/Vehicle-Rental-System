package com.vehiclerental.controllers.metrics;

import com.vehiclerental.models.Rental;
import com.vehiclerental.repositories.RentalRepository;
import com.vehiclerental.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/metrics")
public class VehicleMetricsController {

    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;

    @GetMapping("/vehicles/summary")
    public Map<String, Object> vehicleSummary() {
        long total       = vehicleRepo.count();

        // Vehicle.status is String in your model
        long available   = vehicleRepo.countByStatusIgnoreCase("AVAILABLE");
        long rented      = vehicleRepo.countByStatusIgnoreCase("RENTED");
        long maintenance = vehicleRepo.countByStatusIgnoreCase("MAINTENANCE");
        long unavailable = vehicleRepo.countByStatusIgnoreCase("UNAVAILABLE");

        // Rental.status is enum
        long activeRentals = rentalRepo.countByStatus(Rental.RentalStatus.ACTIVE);

        Double revenue12m = rentalRepo.getRevenueSince(LocalDateTime.now().minusMonths(12));
        if (revenue12m == null) revenue12m = 0.0;

        return Map.of(
                "totalVehicles", total,
                "availableVehicles", available,
                "rentedVehicles", rented,
                "maintenanceVehicles", maintenance,
                "unavailableVehicles", unavailable,
                "activeRentals", activeRentals,
                "revenue12m", revenue12m
        );
    }
}
