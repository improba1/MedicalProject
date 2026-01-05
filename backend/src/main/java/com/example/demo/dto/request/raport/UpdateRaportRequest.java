package com.example.demo.dto.request.raport;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class UpdateRaportRequest {

    private String disease;

    private String symptoms;

    @DecimalMin(value = "0.0")
    private Double price;

    private String notes;

    private String visitStatus;
}