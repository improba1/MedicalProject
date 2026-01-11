package com.example.demo.service.payment;

import com.stripe.exception.SignatureVerificationException;

import java.util.UUID;

public interface PaymentService {

    String createCheckoutSession(UUID visitId);

    void handleWebhook(String payload, String signatureHeader) throws SignatureVerificationException;
}