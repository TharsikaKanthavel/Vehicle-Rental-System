package com.vehiclerental.repositories;

import com.vehiclerental.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByStatus(String status);

    // Case-insensitive counter by status (Vehicle.status is String)
    @Query("select count(v) from Vehicle v where upper(v.status) = upper(:status)")
    long countByStatusIgnoreCase(@Param("status") String status);

    // Convenience counters (optional: used by dashboards)
    @Query("select count(v) from Vehicle v where upper(v.status) = 'AVAILABLE'")
    Long countAvailableVehicles();

    @Query("select count(v) from Vehicle v where upper(v.status) = 'RENTED'")
    Long countRentedVehicles();

    // Simple search across key fields
    @Query("""
        select v from Vehicle v
        where lower(v.make) like lower(concat('%', :q, '%'))
           or lower(v.model) like lower(concat('%', :q, '%'))
           or lower(v.licensePlate) like lower(concat('%', :q, '%'))
    """)
    List<Vehicle> searchVehicles(@Param("q") String q);
}
