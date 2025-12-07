package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PatientResponse{
    private UUID id;
    private String nickname;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String sex;

    private List<UUID> visitIds;
    private List<UUID> raportIds;
}