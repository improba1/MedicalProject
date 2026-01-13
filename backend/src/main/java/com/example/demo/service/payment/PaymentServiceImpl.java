package com.example.demo.service.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.Role;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Payment;
import com.example.demo.model.Visit;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.visit.VisitStatusService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final VisitRepository visitRepository;
    private final PaymentRepository paymentRepository;
    private final VisitStatusService visitStatusService;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${app.frontend.success-url}")
    private String successUrl;

    @Value("${app.frontend.cancel-url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    @Transactional
    public String createCheckoutSession(UUID visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        if (!(visit.getStatus() == VisitStatus.SCHEDULED || visit.getStatus() == VisitStatus.RESCHEDULED)) {
            throw new IllegalStateException("Only scheduled visits can be paid");
        }
        if (visit.getServices() == null || visit.getServices().isEmpty()) {
            throw new IllegalStateException("Visit has no services to pay for");
        }
        BigDecimal totalPrice = visit.getTotalPrice();
        long amountInCents = totalPrice
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("pln")
                                                    .setUnitAmount(amountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Medical Visit Payment")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putMetadata("visitId", visitId.toString())
                    .build();
            Session session = Session.create(params);
            Payment payment = Payment.builder()
                    .visit(visit)
                    .amount(totalPrice)
                    .status(PaymentStatus.PENDING)
                    .stripeSessionId(session.getId())
                    .createdAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
            visit.setCartLocked(true);
            visitRepository.save(visit);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void handleWebhook(String payload, String signatureHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);
        if (!"checkout.session.completed".equals(event.getType())) {
            return;
        }
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Cannot deserialize session"));
        Payment payment = paymentRepository.findByStripeSessionId(session.getId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        Visit visit = payment.getVisit();
        BigDecimal currentTotal = visit.getTotalPrice();
        if (payment.getAmount() == null || payment.getAmount().compareTo(currentTotal) != 0) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new IllegalStateException("Payment amount mismatch with visit total");
        }
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        payment.setStripePaymentIntentId(session.getPaymentIntent());
        paymentRepository.save(payment);
        visitStatusService.pay(visit, Role.PATIENT);
        visitRepository.save(visit);
    }
}