package com.example.demo.dto.request.visit;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateVisitRequest {
    private String status;
    private LocalDateTime newAppointmentTime;
}