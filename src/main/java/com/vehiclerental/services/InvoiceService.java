package com.vehiclerental.services;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    public void generateInvoiceForBooking(Long bookingId) {
        // TODO: integrate OpenPDF/iText + email
        System.out.println("Invoice generated for booking " + bookingId);
    }
}
