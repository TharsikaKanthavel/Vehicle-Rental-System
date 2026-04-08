package com.vehiclerental.delivery;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries",
        indexes = {
                @Index(name="idx_delivery_vehicle_time", columnList="vehicleId,scheduledStart,scheduledEnd"),
                @Index(name="idx_delivery_driver_time", columnList="driverId,scheduledStart,scheduledEnd")
        })
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull private Long vehicleId;        // FK → your Vehicle.id (kept as numeric FK only)
    @NotNull private Long driverId;         // FK → your User.id (driver)
    @NotNull private Long createdByUserId;  // scheduler (staff/admin)

    @NotBlank @Size(min=3, max=80)  private String customerName;
    @NotBlank @Size(min=5, max=200) private String pickupLocation;
    @NotBlank @Size(min=5, max=200) private String dropoffLocation;

    @NotNull private LocalDateTime scheduledStart;
    @NotNull private LocalDateTime scheduledEnd;

    @NotNull @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.SCHEDULED;

    @Size(max=250) private String notes;

    // getters/setters
    public Long getId() { return id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
    public LocalDateTime getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(LocalDateTime scheduledStart) { this.scheduledStart = scheduledStart; }
    public LocalDateTime getScheduledEnd() { return scheduledEnd; }
    public void setScheduledEnd(LocalDateTime scheduledEnd) { this.scheduledEnd = scheduledEnd; }
    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
