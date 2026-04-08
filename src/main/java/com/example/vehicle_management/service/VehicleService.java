package com.example.vehicle_management.service;

import com.example.vehicle_management.entity.Vehicle;
import com.example.vehicle_management.entity.VehicleAvailability;
import com.example.vehicle_management.entity.AvailabilityStatus;
import com.example.vehicle_management.repository.VehicleRepository;
import com.example.vehicle_management.repository.VehicleAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepo;
    private final VehicleAvailabilityRepository availabilityRepo;

    public VehicleService(VehicleRepository vehicleRepo, VehicleAvailabilityRepository availabilityRepo) {
        this.vehicleRepo = vehicleRepo;
        this.availabilityRepo = availabilityRepo;
    }

    // ✅ Save or update a vehicle
    public Vehicle saveVehicle(Vehicle vehicle) {
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepo.save(vehicle);
    }

    // ✅ Get all vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    // ✅ Get vehicle by ID
    public Optional<Vehicle> findById(Long id) {
        return vehicleRepo.findById(id);
    }

    // ✅ Delete vehicle
    public void deleteById(Long id) {
        vehicleRepo.deleteById(id);
    }

    // ✅ Find available vehicles for a time period
    public List<Vehicle> findAvailable(LocalDateTime start, LocalDateTime end) {
        // Step 1: find all bookings/maintenance windows that overlap
        List<VehicleAvailability> overlapping = availabilityRepo.findOverlapping(start, end);

        // Step 2: collect busy vehicle IDs
        Set<Long> busyVehicleIds = overlapping.stream()
                .map(va -> va.getVehicle().getId())
                .collect(Collectors.toSet());

        // Step 3: return all AVAILABLE vehicles not in busy list
        return vehicleRepo.findAll().stream()
                .filter(v -> v.getStatus() == AvailabilityStatus.AVAILABLE)
                .filter(v -> !busyVehicleIds.contains(v.getId()))
                .collect(Collectors.toList());
    }

    // ✅ Create a booking (block vehicle in a given period)
    public VehicleAvailability createBooking(Long vehicleId, LocalDateTime start, LocalDateTime end, String note) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // Check if already booked
        List<Vehicle> available = findAvailable(start, end);
        if (!available.contains(vehicle)) {
            throw new RuntimeException("Vehicle is not available for the selected time range");
        }

        VehicleAvailability booking = new VehicleAvailability(vehicle, start, end, AvailabilityStatus.BOOKED);
        booking.setNote(note);
        return availabilityRepo.save(booking);
    }
}
