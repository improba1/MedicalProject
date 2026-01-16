package com.example.demo.controller.admin;

import com.example.demo.dto.request.payment.PaymentSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.mapper.PaymentMapper;
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
@RequestMapping("${api.prefix}/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentFilterService paymentFilterService;
    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;

    @GetMapping("/get/{paymentId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(
            @PathVariable UUID paymentId
    ) {
        var payment = paymentFilterService.getPaymentById(paymentId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Payment retrieved successfully",
                        paymentMapper.toResponse(payment)
                )
        );
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

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> searchAdmin(@RequestBody PaymentSearchRequest req) {

        var payments = paymentFilterService.searchForAdmin(
                req.getVisitId(),
                req.getPatientId(),
                req.getStatus(),
                req.getMinAmount(),
                req.getMaxAmount(),
                req.getStart(),
                req.getEnd()
        );
        return ResponseEntity.ok(ApiResponse.of(
                200,
                "Filtered payments fetched successfully",
                paymentMapper.toResponseList(payments)
        ));
    }
}