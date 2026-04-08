package com.vehiclerental.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // time overlaps for same vehicle
    @Query("""
     SELECT d FROM Delivery d
      WHERE d.vehicleId = :vehicleId
        AND d.status <> com.vehiclerental.delivery.DeliveryStatus.CANCELLED
        AND d.scheduledStart < :endTs
        AND d.scheduledEnd   > :startTs
  """)
    List<Delivery> findVehicleOverlaps(Long vehicleId, LocalDateTime startTs, LocalDateTime endTs);

    // time overlaps for same driver
    @Query("""
     SELECT d FROM Delivery d
      WHERE d.driverId = :driverId
        AND d.status <> com.vehiclerental.delivery.DeliveryStatus.CANCELLED
        AND d.scheduledStart < :endTs
        AND d.scheduledEnd   > :startTs
  """)
    List<Delivery> findDriverOverlaps(Long driverId, LocalDateTime startTs, LocalDateTime endTs);

    List<Delivery> findByDriverIdOrderByScheduledStartAsc(Long driverId);
    List<Delivery> findByStatusOrderByScheduledStartAsc(DeliveryStatus status);
}
