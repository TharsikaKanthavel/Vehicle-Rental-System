package com.vehiclerental.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maintenance_events")
public class MaintenanceEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @NotBlank
    private String type; // Service / Repair / Inspection

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String notes;
}
