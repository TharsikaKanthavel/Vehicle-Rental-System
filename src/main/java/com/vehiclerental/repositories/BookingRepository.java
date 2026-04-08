package com.vehiclerental.repositories;

import com.vehiclerental.models.Booking;
import com.vehiclerental.models.BookingStatus;
import com.vehiclerental.models.DriverTripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);

    List<Booking> findByAssignedDriverIdAndStatusAndDriverTripStatusInOrderByPickupDateTimeAsc(
            Long driverId, BookingStatus status, Collection<DriverTripStatus> tripStatuses
    );

    // Overlap check against CONFIRMED bookings
    @Query("""
           select b from Booking b
           where b.vehicleId = :vehicleId
             and b.status = com.vehiclerental.models.BookingStatus.CONFIRMED
             and b.pickupDateTime < :ret
             and b.returnDateTime > :pickup
           """)
    List<Booking> findOverlappingConfirmed(Long vehicleId, LocalDateTime pickup, LocalDateTime ret);
}
