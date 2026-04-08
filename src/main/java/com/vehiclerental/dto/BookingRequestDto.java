package com.vehiclerental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public class BookingRequestDto {
    @NotNull private Long vehicleId;
    @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") private LocalDateTime pickupDateTime;
    @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") private LocalDateTime returnDateTime;
    @Size(max = 200) private String pickupLocation;
    @Size(max = 200) private String dropoffLocation;
    private double basePricePerDay;
    public Long getVehicleId() { return vehicleId; } public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public LocalDateTime getPickupDateTime() { return pickupDateTime; } public void setPickupDateTime(LocalDateTime pickupDateTime) { this.pickupDateTime = pickupDateTime; }
    public LocalDateTime getReturnDateTime() { return returnDateTime; } public void setReturnDateTime(LocalDateTime returnDateTime) { this.returnDateTime = returnDateTime; }
    public String getPickupLocation() { return pickupLocation; } public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getDropoffLocation() { return dropoffLocation; } public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
    public double getBasePricePerDay() { return basePricePerDay; } public void setBasePricePerDay(double basePricePerDay) { this.basePricePerDay = basePricePerDay; }
}
