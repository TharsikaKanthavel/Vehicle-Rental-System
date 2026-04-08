package com.vehiclerental.controllers.manager;

import com.vehiclerental.models.MaintenanceEvent;
import com.vehiclerental.models.Vehicle;
import com.vehiclerental.repositories.MaintenanceRepository;
import com.vehiclerental.repositories.VehicleRepository;
import com.vehiclerental.services.AvailabilityService;
import com.vehiclerental.services.MaintenanceService;
import com.vehiclerental.services.VehicleGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class VehicleMgmtRestController {

    private final VehicleRepository vehicleRepository;
    private final VehicleGuardService vehicleGuardService;
    private final AvailabilityService availabilityService;
    private final MaintenanceService maintenanceService;
    private final MaintenanceRepository maintenanceRepository;

    // List / search / filter by status
    @GetMapping
    public List<Vehicle> list(@RequestParam(required = false) String q,
                              @RequestParam(required = false) String status) {
        if (q != null && !q.isBlank()) {
            return vehicleRepository.searchVehicles(q.trim());
        }
        if (status != null && !status.isBlank()) {
            // status is stored as String in Vehicle.status (e.g., AVAILABLE, RENTED, MAINTENANCE)
            return vehicleRepository.findByStatus(status);
        }
        return vehicleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle get(@PathVariable Long id) {
        return vehicleRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Vehicle> create(@Validated @RequestBody Vehicle in) {
        return ResponseEntity.ok(vehicleRepository.save(in));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @Validated @RequestBody Vehicle in) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow();
        v.setMake(in.getMake());
        v.setModel(in.getModel());
        v.setYear(in.getYear());
        v.setLicensePlate(in.getLicensePlate());
        v.setDailyRate(in.getDailyRate());
        v.setType(in.getType());
        v.setColor(in.getColor());
        v.setMileage(in.getMileage());
        v.setStatus(in.getStatus());
        v.setImageUrl(in.getImageUrl());
        v.setDescription(in.getDescription());
        return ResponseEntity.ok(vehicleRepository.save(v));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        vehicleGuardService.ensureDeletable(id);
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Availability ---
    public record AvailabilityResp(boolean available, String reason) {}

    @GetMapping("/{id}/availability")
    public AvailabilityResp isAvailable(@PathVariable Long id,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        boolean ok = availabilityService.isAvailable(id, from, to);
        String reason = availabilityService.firstBlockReason(id, from, to);
        return new AvailabilityResp(ok, reason);
    }

    public record CalendarSeg(@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate start,
                              @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate end,
                              String type) {}

    @GetMapping("/{id}/calendar")
    public List<CalendarSeg> calendar(@PathVariable Long id,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        // TODO: build from rentals + maintenance; returning empty list for now
        return new ArrayList<>();
    }

    // --- Maintenance ---
    public record MaintenanceReq(String type,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                 String notes) {}

    @PostMapping("/{id}/maintenance")
    public MaintenanceEvent schedule(@PathVariable Long id, @RequestBody MaintenanceReq req) {
        return maintenanceService.schedule(id, req.type(), req.start(), req.end(), req.notes());
    }

    @GetMapping("/{id}/maintenance")
    public List<MaintenanceEvent> listMaintenance(@PathVariable Long id) {
        return maintenanceRepository.findByVehicleId(id);
    }
}
