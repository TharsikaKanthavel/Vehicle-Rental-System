package com.vehiclerental.services.gateway;

import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaytmFakeGateway implements FakeGatewayService {
    @Override public String preAuthorize(Long paymentId, double amount) {
        return "PAYTM_FAKE-" + paymentId + "-" + UUID.randomUUID();
    }
    @Override public boolean capture(String providerRef, double amount) {
        return ThreadLocalRandom.current().nextInt(0, 100) > 5; // ~95% success
    }
    @Override public boolean refund(String providerRef, double amount) { return true; }
}
