// src/main/java/com/vehiclerental/inspection/VehicleInspectionRepository.java
package com.vehiclerental.inspection;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehicleInspectionRepository extends JpaRepository<VehicleInspection, Long> {
    Optional<VehicleInspection> findByDeliveryId(Long deliveryId);

    // NEW: list newest first for admin screen
    List<VehicleInspection> findAllByOrderByCreatedAtDesc();
}
