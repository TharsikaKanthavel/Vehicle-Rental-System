package com.vehiclerental.services;

import com.vehiclerental.models.Rental;
import com.vehiclerental.models.User;
import com.vehiclerental.repositories.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;

    public Long getActiveRentalsCount() {
        return rentalRepository.countByStatus(Rental.RentalStatus.ACTIVE);
    }

    public Double getTotalRevenue() {
        Double revenue = rentalRepository.getRevenueSince(LocalDateTime.now().minusYears(1));
        return revenue != null ? revenue : 0.0;
    }

    public Double getMonthlyRevenue() {
        Double revenue = rentalRepository.getRevenueSince(LocalDateTime.now().minusMonths(1));
        return revenue != null ? revenue : 0.0;
    }

    public Integer getPendingPayments() {
        // Replace with a real payments table later if needed
        return rentalRepository.findByStatus(Rental.RentalStatus.PENDING).size();
    }

    // Demo/chart helpers
    public Object getRevenueChartData() {
        return new Object() {
            public final String[] labels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
            public final double[] data   = {12000, 19000, 15000, 25000, 22000, 30000};
        };
    }

    public Object getRevenueReport() {
        return new Object() {
            public final double totalRevenue   = getTotalRevenue();
            public final double monthlyRevenue = getMonthlyRevenue();
            public final int totalRentals      = (int) rentalRepository.count();
        };
    }

    public List<Object> getAllTransactions() { return Collections.emptyList(); }

    public List<Rental> getUserActiveRentals(User user) {
        return rentalRepository.findByUserAndStatus(user, Rental.RentalStatus.ACTIVE);
    }

    public List<Rental> getUserRentalHistory(User user) {
        return rentalRepository.findByUser(user);
    }

    public List<Rental> getUserRentals(User user) {
        return rentalRepository.findByUser(user);
    }

    public List<Rental> getPendingRentals() {
        return rentalRepository.findByStatus(Rental.RentalStatus.PENDING);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    // Stubs for driver features
    public List<Rental> getDriverAssignedRentals(User user) { return Collections.emptyList(); }
    public List<Rental> getDriverCompletedRentals(User user) { return Collections.emptyList(); }
    public List<Rental> getDriverSchedule(User user) { return Collections.emptyList(); }
}
