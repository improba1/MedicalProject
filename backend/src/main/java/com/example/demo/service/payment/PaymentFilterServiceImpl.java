package com.example.demo.service.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.specification.payment.PaymentSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentFilterServiceImpl implements PaymentFilterService {

    private final PaymentRepository paymentRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByIdForAuthenticatedPatient(UUID paymentId) {
        UUID patientId = currentUserService.getAuthenticatedPatient().getId();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if (!payment.getVisit().getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("You can access only your own payments");
        }
        return payment;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Payment> searchForAdmin(UUID visitId, UUID patientId, PaymentStatus status, BigDecimal minAmount, BigDecimal maxAmount, LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findAll(
                PaymentSpecificationBuilder.build(
                        visitId, patientId, status, minAmount, maxAmount, start, end
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> searchForUser(UUID visitId, PaymentStatus status, BigDecimal minAmount, BigDecimal maxAmount, LocalDateTime start, LocalDateTime end) {
        UUID patientId = currentUserService.getAuthenticatedPatient().getId();
        return paymentRepository.findAll(
                PaymentSpecificationBuilder.build(
                        visitId, patientId, status, minAmount, maxAmount, start, end
                )
        );
    }
}