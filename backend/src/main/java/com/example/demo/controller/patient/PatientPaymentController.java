package com.example.demo.controller.patient;

import com.example.demo.dto.request.payment.PatientPaymentSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.payment.PaymentFilterService;
import com.example.demo.service.payment.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/patient/payment")
@RequiredArgsConstructor
public class PatientPaymentController {

    private final PaymentService paymentService;
    private final PaymentFilterService paymentFilterService;
    private final PaymentMapper paymentMapper;
    private final CurrentUserService currentUserService;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> searchMyPayments(
            @RequestBody PatientPaymentSearchRequest request
    ) {
        UUID patientId = currentUserService.getPatientId();
        var payments = paymentFilterService.filter(
                request.getVisitId(),
                request.getDoctorId(),
                patientId,
                request.getStatus(),
                request.getMinAmount(),
                request.getMaxAmount(),
                request.getStart(),
                request.getEnd()
        );

        return ResponseEntity.ok(ApiResponse.of(
                200,
                "Filtered payments fetched successfully",
                paymentMapper.toResponseList(payments)
        ));
    }


    @PostMapping("/pay/{visitId}")
    @PreAuthorize("hasAuthority('patient:create')")
    public ResponseEntity<ApiResponse<String>> pay(@PathVariable UUID visitId) {
        String url = paymentService.createCheckoutSession(visitId);
        return ResponseEntity.ok(ApiResponse.of(200, "Payment session created", url));
    }

    @PostMapping("/stripe/webhook")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader
    ) throws SignatureVerificationException {

        paymentService.handleWebhook(payload, signatureHeader);
        return ResponseEntity.ok("success");
    }
}