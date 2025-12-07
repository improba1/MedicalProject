package com.example.demo.dto.request.doctor;

import com.example.demo.enums.Sex;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class AddDoctorRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phone;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Sex sex;

    @NotBlank
    private String address;

    @NotBlank
    private String specialization;

    @NotBlank
    private String qualification;

    @NotNull
    private LocalDate startDate;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private double rating;

    private MultipartFile image;
}