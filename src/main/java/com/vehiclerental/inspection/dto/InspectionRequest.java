package com.vehiclerental.inspection.dto;

import com.vehiclerental.inspection.Cleanliness;
import com.vehiclerental.inspection.FuelLevel;
import jakarta.validation.constraints.*;

public class InspectionRequest {
    @NotNull public Long deliveryId;
    @NotNull public Long vehicleId;
    @Positive public Integer odometer;
    @NotNull public FuelLevel fuelLevel;
    @NotNull public Cleanliness cleanliness;
    @Size(max=250) public String notes;
}
