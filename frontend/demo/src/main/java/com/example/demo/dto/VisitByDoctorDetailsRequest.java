package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VisitByDoctorDetailsRequest {
    private String doctor_name;
    private String doctor_last_name;
}
