package com.example.demo.dto.response;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponse {
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
    private boolean isActive;
    private String role;
}