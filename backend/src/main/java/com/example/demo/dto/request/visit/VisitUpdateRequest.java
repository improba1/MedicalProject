package com.example.demo.dto.request.visit;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitUpdateRequest {
    private String visitStatus;
    private LocalDateTime newAppointmentTime;
}