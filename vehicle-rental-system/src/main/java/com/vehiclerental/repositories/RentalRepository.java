package com.vehiclerental.repositories;

import com.vehiclerental.models.Rental;
import com.vehiclerental.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    // Count by enum status (use this instead of a non-existent countActiveRentals())
    long countByStatus(Rental.RentalStatus status);

    // Total revenue since a timestamp
    @Query("select coalesce(sum(r.totalAmount), 0) from Rental r where r.createdAt >= :since")
    Double getRevenueSince(@Param("since") LocalDateTime since);

    // User lookups
    List<Rental> findByUser(User user);
    List<Rental> findByStatus(Rental.RentalStatus status);
    List<Rental> findByUserAndStatus(User user, Rental.RentalStatus status);

    // Date-range overlaps (LocalDate-based)
    @Query("""
           select r from Rental r
           where r.vehicle.id = :vehicleId
             and r.startDate < :to
             and r.endDate   > :from
           """)
    List<Rental> findOverlaps(@Param("vehicleId") Long vehicleId,
                              @Param("from") LocalDate from,
                              @Param("to")   LocalDate to);

    boolean existsByVehicleIdAndEndDateGreaterThanEqualAndStatus(
            Long vehicleId,
            java.time.LocalDate today,
            com.vehiclerental.models.Rental.RentalStatus status
    );


}
