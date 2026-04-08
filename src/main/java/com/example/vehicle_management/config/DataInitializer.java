package com.example.vehicle_management.config;

import com.example.vehicle_management.entity.AvailabilityStatus;
import com.example.vehicle_management.entity.Vehicle;
import com.example.vehicle_management.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(VehicleRepository vehicleRepo) {
        return args -> {
            if (vehicleRepo.count() == 0) {
                Vehicle v1 = new Vehicle();
                v1.setPlateNumber("WP-ABC-123");
                v1.setBrand("Toyota");
                v1.setModel("Axio");
                v1.setType("Sedan");
                v1.setMileage(60000);
                v1.setDailyRate(4500.0);
                v1.setStatus(AvailabilityStatus.AVAILABLE);

                Vehicle v2 = new Vehicle();
                v2.setPlateNumber("WP-XYZ-456");
                v2.setBrand("Nissan");
                v2.setModel("Caravan");
                v2.setType("Van");
                v2.setMileage(120000);
                v2.setDailyRate(7500.0);
                v2.setStatus(AvailabilityStatus.AVAILABLE);

                vehicleRepo.save(v1);
                vehicleRepo.save(v2);

                System.out.println("✅ Sample vehicles inserted into DB");
            }
        };
    }
}

