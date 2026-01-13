package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "raports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Raport {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false, unique = true)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String disease;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Lob
    private String treatmentPlan;

    @Lob
    private String doctorNotes;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Lob
    @Column(nullable = false)
    private String servicesSnapshot;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    private String paymentReceipt;
}