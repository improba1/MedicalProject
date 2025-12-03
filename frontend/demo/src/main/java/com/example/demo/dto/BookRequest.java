package com.example.demo.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookRequest {
    private UUID id_of_doctor_schedule;
    private UUID patient_id;
    private UUID doctor_id;
}
