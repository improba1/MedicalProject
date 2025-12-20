package com.example.demo.dto.request.doctor;

import com.example.demo.enums.Specialization;
import lombok.Data;

@Data
public class DoctorSearchRequest {
    private String name;
    private Specialization specialization;
    private Double rating;
}