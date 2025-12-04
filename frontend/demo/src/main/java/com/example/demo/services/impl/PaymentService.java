package com.example.demo.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.demo.repos.PaymentRepository;
import com.example.demo.repos.VisitRepository;
import com.example.demo.services.IPaymentService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.StripeObject;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;
import com.example.demo.model.Visit;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.transaction.annotation.Transactional;
import com.stripe.model.checkout.Session;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final VisitRepository visitRepository;
    private final PaymentRepository paymentRepository;

    @Value("${STRIPE_API_KEY}")
    private String apiKey;

    @Value("${WEBHOOK_SECRET}")
    private String webhookSecret;

    @Override
    @Transactional
    public String createCheckoutSession(UUID visit_id) {
        Visit visit = visitRepository.findById(visit_id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found with id: " + visit_id));

        Stripe.apiKey = apiKey;
        int price = 5000; //50 zloty //scheduleRepo.findByVisitId(visitId)

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/api/payments/success")
                    .setCancelUrl("http://localhost:8080/api/payments/cancel")
                    .putMetadata("visit_id", visit_id.toString())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("pln")
                                                    .setUnitAmount((long) price)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Visit " + visit_id)
                                                                    .build())
                                                    .build())
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            Payment payment = visit.getPayment();
            if (payment == null) {
                payment = Payment.builder()
                        .visit(visit)
                        .amount(price)
                        .status(PaymentStatus.PENDING)
                        .started_at(LocalDateTime.now())
                        .build();
            }
            payment.setStripeSessionId(session.getId());
            payment.setStarted_at(LocalDateTime.now());
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);
            visit.setPayment(payment);
            visitRepository.save(visit);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe session", e);
        }
    }

    @Override
    @Transactional
    public void handleWebhook(String payload, String signature) {
        Stripe.apiKey = apiKey;
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid signature", e);
        }

        if ("checkout.session.completed".equals(event.getType())) {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow();
            String sessionId = ((Session) stripeObject).getId();

            if (sessionId != null) {
                paymentRepository.findByStripeSessionId(sessionId).ifPresent(payment -> {
                    payment.setStatus(PaymentStatus.PAID);
                    payment.setPaid_at(LocalDateTime.now());
                    paymentRepository.save(payment);
                });
            }
        }
    }
}
