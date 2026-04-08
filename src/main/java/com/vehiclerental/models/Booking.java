package com.vehiclerental.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long vehicleId;

    @NotNull
    @Column(name = "pickup_date_time", nullable = false)
    private LocalDateTime pickupDateTime;

    @NotNull
    @Column(name = "return_date_time", nullable = false)
    private LocalDateTime returnDateTime;

    // DB columns that are NOT NULL
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 200)
    private String pickupLocation;

    @Column(length = 200)
    private String dropoffLocation;

    private double basePricePerDay;
    private double estimatedTotal;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING_APPROVAL;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Long paymentId;

    private Long assignedDriverId;     // nullable until assignment
    private String driverNote;

    @Enumerated(EnumType.STRING)
    private DriverTripStatus driverTripStatus = DriverTripStatus.ASSIGNED;

    public Booking() {}

    /* Keep startDate/endDate in sync with pickup/return before insert/update */
    @PrePersist
    @PreUpdate
    private void syncDates() {
        if (pickupDateTime != null) this.startDate = pickupDateTime.toLocalDate();
        if (returnDateTime != null) this.endDate = returnDateTime.toLocalDate();
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public LocalDateTime getPickupDateTime() { return pickupDateTime; }
    public void setPickupDateTime(LocalDateTime pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
        if (pickupDateTime != null) this.startDate = pickupDateTime.toLocalDate();
    }

    public LocalDateTime getReturnDateTime() { return returnDateTime; }
    public void setReturnDateTime(LocalDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
        if (returnDateTime != null) this.endDate = returnDateTime.toLocalDate();
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public double getBasePricePerDay() { return basePricePerDay; }
    public void setBasePricePerDay(double basePricePerDay) { this.basePricePerDay = basePricePerDay; }

    public double getEstimatedTotal() { return estimatedTotal; }
    public void setEstimatedTotal(double estimatedTotal) { this.estimatedTotal = estimatedTotal; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getAssignedDriverId() { return assignedDriverId; }
    public void setAssignedDriverId(Long assignedDriverId) { this.assignedDriverId = assignedDriverId; }

    public String getDriverNote() { return driverNote; }
    public void setDriverNote(String driverNote) { this.driverNote = driverNote; }

    public DriverTripStatus getDriverTripStatus() { return driverTripStatus; }
    public void setDriverTripStatus(DriverTripStatus driverTripStatus) { this.driverTripStatus = driverTripStatus; }
}
