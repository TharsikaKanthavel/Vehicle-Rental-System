package com.vehiclerental.services.gateway;

public interface FakeGatewayService {
    String preAuthorize(Long paymentId, double amount);
    boolean capture(String providerRef, double amount);
    boolean refund(String providerRef, double amount);
}
