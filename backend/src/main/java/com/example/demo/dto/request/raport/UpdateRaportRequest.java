package com.example.demo.dto.request.raport;

import lombok.Data;

@Data
public class UpdateRaportRequest {

    private String disease;

    private String treatmentPlan;

    private String doctorNotes;
}