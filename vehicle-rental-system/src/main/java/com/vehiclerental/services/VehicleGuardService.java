package com.vehiclerental.services;

import com.vehiclerental.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleGuardService {
    private final VehicleRepository vehicleRepository;
    private final AvailabilityService availabilityService;

    public void ensureDeletable(Long vehicleId) {
        if (availabilityService.hasActiveOrFuture(vehicleId)) {
            throw new IllegalStateException("Booked vehicle cannot be removed");
        }
        // ok to delete
    }
}
