package com.example.demo.controller.patient;

import com.example.demo.dto.request.payment.PaymentSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.service.payment.PaymentFilterService;
import com.example.demo.service.payment.PaymentService;
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

    @GetMapping("/get/{paymentId}")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getMyPaymentById(
            @PathVariable UUID paymentId
    ) {
        var payment = paymentFilterService.getPaymentByIdForAuthenticatedPatient(paymentId);
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Payment retrieved successfully",
                        paymentMapper.toResponse(payment)
                )
        );
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> searchOwn(@RequestBody PaymentSearchRequest req) {
        var payments = paymentFilterService.searchForUser(
                req.getVisitId(),
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

    @PostMapping("/pay/{visitId}")
    @PreAuthorize("hasAuthority('patient:create')")
    public ResponseEntity<ApiResponse<String>> pay(@PathVariable UUID visitId) {
        String url = paymentService.createCheckoutSession(visitId);
        return ResponseEntity.ok(ApiResponse.of(200, "Payment session created", url));
    }
}