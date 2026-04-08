package com.vehiclerental.delivery;

import com.vehiclerental.delivery.dto.DeliveryRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository repo;

    // business hours (08:00–20:00) – adjust if you want to externalize
    private static final LocalTime OPEN = LocalTime.of(8,0);
    private static final LocalTime CLOSE = LocalTime.of(20,0);

    @Override
    @Transactional
    public Delivery create(Long schedulerUserId, DeliveryRequest r) {
        validateWindow(r.scheduledStart, r.scheduledEnd);
        validateWithinHours(r.scheduledStart, r.scheduledEnd);
        if (!isVehicleFree(r.vehicleId, r.scheduledStart, r.scheduledEnd))
            throw new ValidationException("Vehicle is not available for the selected window.");
        if (!isDriverFree(r.driverId, r.scheduledStart, r.scheduledEnd))
            throw new ValidationException("Driver is not available for the selected window.");

        Delivery d = new Delivery();
        d.setVehicleId(r.vehicleId);
        d.setDriverId(r.driverId);
        d.setCreatedByUserId(schedulerUserId);
        d.setCustomerName(r.customerName);
        d.setPickupLocation(r.pickupLocation);
        d.setDropoffLocation(r.dropoffLocation);
        d.setScheduledStart(r.scheduledStart);
        d.setScheduledEnd(r.scheduledEnd);
        d.setNotes(r.notes);
        d.setStatus(DeliveryStatus.SCHEDULED);
        return repo.save(d);
    }

    @Override
    @Transactional
    public Delivery update(Long id, DeliveryRequest r) {
        Delivery d = repo.findById(id).orElseThrow(() -> new ValidationException("Delivery not found"));
        if (d.getStatus() == DeliveryStatus.COMPLETED || d.getStatus() == DeliveryStatus.CANCELLED)
            throw new ValidationException("Completed/Cancelled deliveries cannot be edited.");

        validateWindow(r.scheduledStart, r.scheduledEnd);
        validateWithinHours(r.scheduledStart, r.scheduledEnd);

        // exclude self from conflict checks by cancelling then re-applying check
        if (conflictsVehicleExcept(id, r.vehicleId, r.scheduledStart, r.scheduledEnd))
            throw new ValidationException("Vehicle conflict with another delivery.");
        if (conflictsDriverExcept(id, r.driverId, r.scheduledStart, r.scheduledEnd))
            throw new ValidationException("Driver conflict with another delivery.");

        d.setVehicleId(r.vehicleId);
        d.setDriverId(r.driverId);
        d.setCustomerName(r.customerName);
        d.setPickupLocation(r.pickupLocation);
        d.setDropoffLocation(r.dropoffLocation);
        d.setScheduledStart(r.scheduledStart);
        d.setScheduledEnd(r.scheduledEnd);
        d.setNotes(r.notes);
        return repo.save(d);
    }

    @Override @Transactional
    public void cancel(Long id) {
        Delivery d = repo.findById(id).orElseThrow(() -> new ValidationException("Delivery not found"));
        d.setStatus(DeliveryStatus.CANCELLED);
        repo.save(d);
    }

    @Override @Transactional
    public Delivery start(Long id) {
        Delivery d = repo.findById(id).orElseThrow(() -> new ValidationException("Delivery not found"));
        if (d.getStatus() != DeliveryStatus.SCHEDULED) throw new ValidationException("Only scheduled deliveries can be started.");
        d.setStatus(DeliveryStatus.IN_PROGRESS);
        return repo.save(d);
    }

    @Override @Transactional
    public Delivery complete(Long id) {
        Delivery d = repo.findById(id).orElseThrow(() -> new ValidationException("Delivery not found"));
        if (d.getStatus() != DeliveryStatus.IN_PROGRESS) throw new ValidationException("Only in-progress deliveries can be completed.");
        d.setStatus(DeliveryStatus.COMPLETED);
        return repo.save(d);
    }

    @Override
    public List<Delivery> listForDriver(Long driverId) { return repo.findByDriverIdOrderByScheduledStartAsc(driverId); }

    @Override
    public List<Delivery> listByStatus(DeliveryStatus status) { return repo.findByStatusOrderByScheduledStartAsc(status); }

    @Override
    public boolean isVehicleFree(Long vehicleId, LocalDateTime s, LocalDateTime e) {
        return repo.findVehicleOverlaps(vehicleId, s, e).isEmpty();
    }

    @Override
    public boolean isDriverFree(Long driverId, LocalDateTime s, LocalDateTime e) {
        return repo.findDriverOverlaps(driverId, s, e).isEmpty();
    }

    private void validateWindow(LocalDateTime s, LocalDateTime e) {
        if (s == null || e == null) throw new ValidationException("Start and end must be provided.");
        if (!e.isAfter(s)) throw new ValidationException("End must be after start.");
        if (s.isBefore(LocalDateTime.now())) throw new ValidationException("Start cannot be in the past.");
    }

    private void validateWithinHours(LocalDateTime s, LocalDateTime e) {
        if (s.toLocalTime().isBefore(OPEN) || e.toLocalTime().isAfter(CLOSE))
            throw new ValidationException("Delivery must be scheduled within working hours (08:00–20:00).");
    }

    private boolean conflictsVehicleExcept(Long id, Long vehicleId, LocalDateTime s, LocalDateTime e) {
        return repo.findVehicleOverlaps(vehicleId, s, e).stream().anyMatch(d -> !d.getId().equals(id));
    }

    private boolean conflictsDriverExcept(Long id, Long driverId, LocalDateTime s, LocalDateTime e) {
        return repo.findDriverOverlaps(driverId, s, e).stream().anyMatch(d -> !d.getId().equals(id));
    }
}
