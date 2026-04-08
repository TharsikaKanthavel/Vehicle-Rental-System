package com.vehiclerental.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;

    @Column(name = "year_made")
    private Integer year;

    @Column(name = "daily_rate", precision = 12, scale = 2)
    private BigDecimal dailyRate;

    // Keep as String
    private String type;          // e.g. SEDAN, SUV
    private String color;

    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    private Integer mileage;

    // e.g. AVAILABLE, RENTED, MAINTENANCE, UNAVAILABLE
    private String status;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String imageUrl;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String description;
}
