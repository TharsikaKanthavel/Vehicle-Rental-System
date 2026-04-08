package com.vehiclerental.delivery.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class DeliveryRequest {
    @NotNull public Long vehicleId;
    @NotNull public Long driverId;
    @NotBlank @Size(min=3,max=80)   public String customerName;
    @NotBlank @Size(min=5,max=200)  public String pickupLocation;
    @NotBlank @Size(min=5,max=200)  public String dropoffLocation;
    @NotNull public LocalDateTime scheduledStart;
    @NotNull public LocalDateTime scheduledEnd;
    @Size(max=250) public String notes;
}
