package com.example.demo.dto.request.raport;

import lombok.Data;

@Data
public class RaportUpdateRequest {

    private String disease;

    private String treatmentPlan;

    private String doctorNotes;
}