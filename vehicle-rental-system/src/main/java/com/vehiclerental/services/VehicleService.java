package com.vehiclerental.services;

import com.vehiclerental.models.Vehicle;
import com.vehiclerental.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Long getAvailableVehiclesCount() {
        return vehicleRepository.countAvailableVehicles();
    }

    // Demo data for a chart (optional)
    public Object getVehiclePerformanceReport() {
        return new Object() {
            public final String[] labels = {"Toyota Camry", "Honda CR-V", "Ford Focus", "BMW X5"};
            public final int[] rentals = {45, 38, 22, 15};
            public final double[] revenue = {12500.00, 9800.00, 5500.00, 4200.00};
        };
    }

    public List<Vehicle> getAllVehicles() { return vehicleRepository.findAll(); }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus("AVAILABLE");
    }

    public Long getRentedVehiclesCount() { return vehicleRepository.countRentedVehicles(); }

    public List<Vehicle> getVehiclesNeedingMaintenance() {
        return vehicleRepository.findByStatus("MAINTENANCE");
    }

    public Optional<Vehicle> findById(Long id) { return vehicleRepository.findById(id); }

    @Transactional
    public Vehicle save(Vehicle vehicle) { return vehicleRepository.save(vehicle); }

    @Transactional
    public void deleteVehicle(Long id) { vehicleRepository.deleteById(id); }

    public List<Vehicle> searchVehicles(String searchTerm) {
        return vehicleRepository.searchVehicles(searchTerm);
    }

    public List<Vehicle> findAll() { return vehicleRepository.findAll(); }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));
    }
    // --- ADD: expose daily price as a double for controllers ---
    public double getVehiclePricePerDay(Long vehicleId) {
        var v = getById(vehicleId); // you already have this method
        return v.getDailyRate() != null ? v.getDailyRate().doubleValue() : 0.0;
    }



    /** Optional: seed a couple of vehicles if DB is empty. */
    @Transactional
    public void initializeSampleVehicles() {
        if (vehicleRepository.count() == 0) {
            Vehicle v1 = Vehicle.builder()
                    .make("Toyota")
                    .model("Camry")
                    .year(2023)
                    .licensePlate("ABC123")
                    .dailyRate(new BigDecimal("45.00"))
                    .type("SEDAN")
                    .color("White")
                    .mileage(15000)
                    .status("AVAILABLE")
                    .imageUrl(null)
                    .description("Comfortable midsize sedan")
                    .build();
            vehicleRepository.save(v1);

            Vehicle v2 = Vehicle.builder()
                    .make("Honda")
                    .model("CR-V")
                    .year(2023)
                    .licensePlate("XYZ789")
                    .dailyRate(new BigDecimal("55.00"))
                    .type("SUV")
                    .color("Black")
                    .mileage(10000)
                    .status("AVAILABLE")
                    .imageUrl(null)
                    .description("Popular compact SUV")
                    .build();
            vehicleRepository.save(v2);
        }
    }
}
