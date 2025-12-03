package com.example.demo.services;

import java.util.UUID;

public interface IPaymentService {
    String createCheckoutSession(UUID visit_id);
    void handleWebhook(String payload, String signature);
}
