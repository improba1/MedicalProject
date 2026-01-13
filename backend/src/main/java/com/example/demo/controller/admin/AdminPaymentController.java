package com.example.demo.controller.admin;

import com.example.demo.dto.request.payment.AdminPaymentSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.mapper.PaymentMapper;
import com.example.demo.service.payment.PaymentFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentFilterService paymentFilterService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> searchPayments(
            @RequestBody AdminPaymentSearchRequest request
    ) {
        var payments = paymentFilterService.filter(
                request.getVisitId(),
                request.getDoctorId(),
                request.getPatientId(),
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
}