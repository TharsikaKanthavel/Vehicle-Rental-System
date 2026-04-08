package com.vehiclerental.services;

import com.vehiclerental.models.Payment;
import com.vehiclerental.models.PaymentStatus;
import com.vehiclerental.repositories.PaymentRepository;
import com.vehiclerental.services.gateway.FakeGatewayService;
import com.vehiclerental.services.gateway.PayPalFakeGateway;
import com.vehiclerental.services.gateway.PaytmFakeGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final FakeGatewayService payPalGateway;
    private final FakeGatewayService paytmGateway;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PayPalFakeGateway payPalGateway,
                              PaytmFakeGateway paytmGateway) {
        this.paymentRepository = paymentRepository;
        this.payPalGateway = payPalGateway;
        this.paytmGateway = paytmGateway;
    }

    @Override
    public Payment initiatePreAuth(Long bookingId, double amount, String method) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
        Payment p = new Payment();
        p.setBookingId(bookingId);
        p.setAmount(amount);
        p.setMethod(method);
        p.setStatus(PaymentStatus.INITIATED);
        p = paymentRepository.save(p);

        String providerRef = ("PAYPAL_FAKE".equals(method)
                ? payPalGateway.preAuthorize(p.getId(), amount)
                : paytmGateway.preAuthorize(p.getId(), amount));
        p.setProviderRef(providerRef);
        p.setStatus(PaymentStatus.PRE_AUTHORIZED);
        return paymentRepository.save(p);
    }

    @Override
    public Payment capture(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElseThrow();
        boolean ok = ("PAYPAL_FAKE".equals(p.getMethod())
                ? payPalGateway.capture(p.getProviderRef(), p.getAmount())
                : paytmGateway.capture(p.getProviderRef(), p.getAmount()));
        p.setStatus(ok ? PaymentStatus.CAPTURED : PaymentStatus.FAILED);
        return paymentRepository.save(p);
    }

    @Override
    public Payment refund(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElseThrow();
        boolean ok = ("PAYPAL_FAKE".equals(p.getMethod())
                ? payPalGateway.refund(p.getProviderRef(), p.getAmount())
                : paytmGateway.refund(p.getProviderRef(), p.getAmount()));
        p.setStatus(ok ? PaymentStatus.REFUNDED : PaymentStatus.FAILED);
        return paymentRepository.save(p);
    }
}
