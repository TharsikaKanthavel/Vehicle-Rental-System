package com.vehiclerental.services.gateway;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PayPalFakeGateway implements FakeGatewayService {
    @Override public String preAuthorize(Long paymentId, double amount) {
        return "PAYPAL_FAKE-" + paymentId + "-" + UUID.randomUUID();
    }
    @Override public boolean capture(String providerRef, double amount) { return amount < 100000; }
    @Override public boolean refund(String providerRef, double amount) { return true; }
}
