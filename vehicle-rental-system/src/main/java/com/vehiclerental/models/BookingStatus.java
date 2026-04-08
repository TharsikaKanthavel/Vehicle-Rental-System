package com.vehiclerental.models;

public enum BookingStatus {
    PENDING_APPROVAL,
    REJECTED,
    CONFIRMED,     // Admin approved + payment captured
    CANCELLED,
    COMPLETED
}
