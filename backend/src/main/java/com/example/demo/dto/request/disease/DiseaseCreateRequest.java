package com.example.demo.dto.request.disease;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DiseaseCreateRequest {

    @NotBlank(message = "Disease name is required")
    @Size(min = 2, max = 100, message = "Disease name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Disease code is required")
    @Size(min = 2, max = 7, message = "Disease code must be between 2 and 7 characters")
    private String diseaseCode;
}