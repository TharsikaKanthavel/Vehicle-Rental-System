package com.vehiclerental.inspection;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name="vehicle_inspections", uniqueConstraints = {
        @UniqueConstraint(name="uk_inspection_per_delivery", columnNames = {"deliveryId"})
})
public class VehicleInspection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull private Long deliveryId;
    @NotNull private Long vehicleId;
    @NotNull private Long inspectedByUserId;

    @Positive private Integer odometer;
    @NotNull @Enumerated(EnumType.STRING) private FuelLevel fuelLevel;
    @NotNull @Enumerated(EnumType.STRING) private Cleanliness cleanliness;
    @Size(max=250) private String notes;

    @NotBlank private String photoFrontPath;
    @NotBlank private String photoRearPath;
    @NotBlank private String photoLeftPath;
    @NotBlank private String photoRightPath;
    @NotBlank private String photoInteriorPath;
    @NotBlank private String photoDashboardPath;

    @NotNull private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters omitted for brevity
    public Long getId() { return id; }
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getInspectedByUserId() { return inspectedByUserId; }
    public void setInspectedByUserId(Long inspectedByUserId) { this.inspectedByUserId = inspectedByUserId; }
    public Integer getOdometer() { return odometer; }
    public void setOdometer(Integer odometer) { this.odometer = odometer; }
    public FuelLevel getFuelLevel() { return fuelLevel; }
    public void setFuelLevel(FuelLevel fuelLevel) { this.fuelLevel = fuelLevel; }
    public Cleanliness getCleanliness() { return cleanliness; }
    public void setCleanliness(Cleanliness cleanliness) { this.cleanliness = cleanliness; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getPhotoFrontPath() { return photoFrontPath; }
    public void setPhotoFrontPath(String s) { this.photoFrontPath = s; }
    public String getPhotoRearPath() { return photoRearPath; }
    public void setPhotoRearPath(String s) { this.photoRearPath = s; }
    public String getPhotoLeftPath() { return photoLeftPath; }
    public void setPhotoLeftPath(String s) { this.photoLeftPath = s; }
    public String getPhotoRightPath() { return photoRightPath; }
    public void setPhotoRightPath(String s) { this.photoRightPath = s; }
    public String getPhotoInteriorPath() { return photoInteriorPath; }
    public void setPhotoInteriorPath(String s) { this.photoInteriorPath = s; }
    public String getPhotoDashboardPath() { return photoDashboardPath; }
    public void setPhotoDashboardPath(String s) { this.photoDashboardPath = s; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
