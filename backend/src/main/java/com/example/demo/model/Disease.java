package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "diseases")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Disease {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "disease_code", nullable = false, length = 7)
    private String diseaseCode;
}