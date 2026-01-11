package com.example.demo.service.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.specification.payment.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentFilterServiceImpl implements PaymentFilterService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> filter(
            UUID visitId,
            UUID doctorId,
            UUID patientId,
            PaymentStatus status,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDateTime start,
            LocalDateTime end
    ) {
        Specification<Payment> spec = PaymentSpecification.byFilters(
                visitId,
                doctorId,
                patientId,
                status,
                minAmount,
                maxAmount,
                start,
                end
        );

        return paymentRepository.findAll(spec);
    }
}