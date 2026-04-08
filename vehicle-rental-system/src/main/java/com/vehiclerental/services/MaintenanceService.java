package com.vehiclerental.services;

import com.vehiclerental.models.MaintenanceEvent;
import com.vehiclerental.models.Vehicle;
import com.vehiclerental.repositories.MaintenanceRepository;
import com.vehiclerental.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Transactional
    public MaintenanceEvent schedule(Long vehicleId, String type, LocalDate start, LocalDate end, String notes) {
        Vehicle v = vehicleRepository.findById(vehicleId).orElseThrow();
        if (!start.isBefore(end)) throw new IllegalArgumentException("Invalid date range");
        boolean overlaps = !maintenanceRepository.findOverlaps(vehicleId, start, end).isEmpty();
        if (overlaps) throw new IllegalStateException("Maintenance overlaps an existing window");

        MaintenanceEvent m = new MaintenanceEvent();
        m.setVehicle(v);
        m.setType((type == null || type.isBlank()) ? "Maintenance" : type.trim());
        m.setStartDate(start);
        m.setEndDate(end);
        m.setNotes(notes);
        return maintenanceRepository.save(m);
    }

    public List<MaintenanceEvent> list(Long vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }
}
