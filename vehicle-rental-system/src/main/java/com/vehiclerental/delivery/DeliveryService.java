package com.vehiclerental.delivery;

import com.vehiclerental.delivery.dto.DeliveryRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryService {
    Delivery create(Long schedulerUserId, DeliveryRequest req);
    Delivery update(Long id, DeliveryRequest req);
    void cancel(Long id);
    Delivery start(Long id);
    Delivery complete(Long id);

    List<Delivery> listForDriver(Long driverId);
    List<Delivery> listByStatus(DeliveryStatus status);

    boolean isVehicleFree(Long vehicleId, LocalDateTime start, LocalDateTime end);
    boolean isDriverFree(Long driverId, LocalDateTime start, LocalDateTime end);
}
