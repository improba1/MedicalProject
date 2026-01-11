package com.example.demo.dto.request.visit;

import com.example.demo.enums.VisitStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VisitSearchRequest {

    private UUID doctorId;
    private UUID patientId;
    private VisitStatus status;
    private LocalDateTime start;
    private LocalDateTime end;
}