package com.example.vehicle_management.repository;

import com.example.vehicle_management.entity.VehicleAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleAvailabilityRepository extends JpaRepository<VehicleAvailability, Long> {

    /**
     * Finds all bookings/maintenance records that overlap with a given time range.
     * Overlap rule: (existing.start < newEnd) AND (existing.end > newStart)
     */
    @Query("SELECT va FROM VehicleAvailability va " +
            "WHERE va.startTime < :end AND va.endTime > :start")
    List<VehicleAvailability> findOverlapping(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    // Optional: find all availability records for a specific vehicle
    List<VehicleAvailability> findByVehicleId(Long vehicleId);
}

