package com.vehiclerental.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private double amount;
    private String currency = "LKR";
    private String method;      // PAYPAL_FAKE / PAYTM_FAKE
    private String providerRef; // fake gateway tx id

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.INITIATED;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Payment() {}

    // getters/setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public double getAmount() { return amount; } public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public String getMethod() { return method; } public void setMethod(String method) { this.method = method; }
    public String getProviderRef() { return providerRef; } public void setProviderRef(String providerRef) { this.providerRef = providerRef; }
    public PaymentStatus getStatus() { return status; } public void setStatus(PaymentStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
