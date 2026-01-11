package com.example.demo.model;

import com.example.demo.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "visits")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Visit {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Raport raport;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    // 🔥 Нове: список послуг, обраних для цього візиту
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitServiceItem> services;

    // 🔥 Нове: симптоми пацієнта (після оплати)
    @Lob
    private String patientSymptoms;

    // 🔥 Зручний метод для підрахунку загальної ціни
    public BigDecimal getTotalPrice() {
        return services == null
                ? BigDecimal.ZERO
                : services.stream()
                .map(VisitServiceItem::getPriceAtMomentOfPurchase)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}