package com.example.demo.model;

import com.example.demo.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
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

    @Column(nullable = false)
    private String symptoms;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status", nullable = false)
    private VisitStatus visitStatus;
}