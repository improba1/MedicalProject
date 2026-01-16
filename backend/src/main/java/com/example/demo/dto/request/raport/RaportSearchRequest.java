package com.example.demo.dto.request.raport;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RaportSearchRequest {

    private UUID visitId;
    private UUID doctorId;
    private UUID patientId;

    private String disease;
    private String paymentReceipt;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private LocalDateTime from;
    private LocalDateTime to;
}