package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "doctor_availability",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"doctor_id", "available_time"})
        }
)
public class DoctorAvailability {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "doctor_id", nullable = false, columnDefinition = "uuid")
    private UUID doctorId;

    @Column(name = "available_time")
    private LocalDateTime availableTime;

    @Column(name = "is_active")
    private boolean isActive;
}