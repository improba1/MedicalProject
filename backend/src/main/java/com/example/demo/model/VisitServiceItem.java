package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "visit_service_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitServiceItem {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;


    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private MedicalService service;


    @Column(nullable = false)
    private String serviceName;


    @Column(nullable = false)
    private BigDecimal priceAtMomentOfPurchase;


    @Column(nullable = false)
    private Integer quantity;
}