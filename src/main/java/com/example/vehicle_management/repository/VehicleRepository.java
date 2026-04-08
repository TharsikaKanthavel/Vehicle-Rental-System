package com.example.vehicle_management.repository;

import com.example.vehicle_management.entity.AvailabilityStatus;
import com.example.vehicle_management.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // find by plate number (unique)
    Optional<Vehicle> findByPlateNumber(String plateNumber);

    // find all vehicles by status (e.g., AVAILABLE, BOOKED, etc.)
    List<Vehicle> findByStatus(AvailabilityStatus status);

    // find all vehicles by type (e.g., Sedan, SUV, Van)
    List<Vehicle> findByType(String type);
}
