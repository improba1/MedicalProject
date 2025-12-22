package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class DoctorResponse {
    private UUID id;
    private String nickname;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private int age;
    private String sex;

    private String specialization;
    private String qualification;
    private LocalDate startDate;
    private int experienceYears;
    private double rating;

    private ImageResponse image;
    private String role;
}