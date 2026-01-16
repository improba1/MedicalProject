package com.example.demo.dto.request.doctor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DoctorUpdateRequest {
    private String address;
    private String qualification;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double rating;

    private MultipartFile image;
}