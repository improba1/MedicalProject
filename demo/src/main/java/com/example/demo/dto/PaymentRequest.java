package com.example.demo.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {
    private UUID visit_id;
    private UUID patient_id;
}
