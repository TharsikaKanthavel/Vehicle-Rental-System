package com.vehiclerental.controllers.manager;

import com.vehiclerental.repositories.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
public class RentalReadController {

    private final RentalRepository rentalRepository;

    public record Seg(String start, String end) {}

    @GetMapping("/vehicle/{vehicleId}")
    public List<Seg> byVehicleAndRange(@PathVariable Long vehicleId,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return rentalRepository.findOverlaps(vehicleId, from, to)
                .stream()
                .map(r -> new Seg(r.getStartDate().toString(), r.getEndDate().toString()))
                .toList();
    }
}
