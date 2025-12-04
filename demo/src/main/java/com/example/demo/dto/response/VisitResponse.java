package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class VisitResponse {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private LocalDateTime appointmentTime;
    private String status;
    private UUID raportId;
}