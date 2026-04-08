package com.example.vehicle_management.controller;

import com.example.vehicle_management.entity.Vehicle;
import com.example.vehicle_management.service.VehicleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*") // allow calls from browser (for HTML/JS fetch)
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // ✅ 1. Get all vehicles
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    // ✅ 2. Add a new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.ok(saved);
    }

    // ✅ 3. Get one vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return vehicleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 4. Update vehicle
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle updated) {
        return vehicleService.findById(id).map(v -> {
            v.setPlateNumber(updated.getPlateNumber());
            v.setBrand(updated.getBrand());
            v.setModel(updated.getModel());
            v.setType(updated.getType());
            v.setMileage(updated.getMileage());
            v.setDailyRate(updated.getDailyRate());
            v.setStatus(updated.getStatus());
            Vehicle saved = vehicleService.saveVehicle(v);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ 5. Delete vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        return vehicleService.findById(id).map(v -> {
            vehicleService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ 6. Find available vehicles in date range
    @GetMapping("/available")
    public ResponseEntity<List<Vehicle>> findAvailableVehicles(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        if (to.isBefore(from) || to.equals(from)) {
            return ResponseEntity.badRequest().build();
        }
        List<Vehicle> available = vehicleService.findAvailable(from, to);
        return ResponseEntity.ok(available);
    }
}
