package com.vehiclerental.services;

import com.vehiclerental.models.Payment;

public interface PaymentService {
    Payment initiatePreAuth(Long bookingId, double amount, String method);
    Payment capture(Long paymentId);
    Payment refund(Long paymentId);
}
