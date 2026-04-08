package com.vehiclerental.repositories;

import com.vehiclerental.models.MaintenanceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceEvent, Long> {

    /**
     * Returns maintenance events that overlap the given [from, to) range for a vehicle.
     */
    @Query("""
        select m from MaintenanceEvent m
        where m.vehicle.id = :vehicleId
          and m.startDate < :to
          and m.endDate   > :from
    """)
    List<MaintenanceEvent> findOverlaps(@Param("vehicleId") Long vehicleId,
                                        @Param("from") LocalDate from,
                                        @Param("to") LocalDate to);

    List<MaintenanceEvent> findByVehicleId(Long vehicleId);
}
