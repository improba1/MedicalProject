package com.example.demo.dto.request.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePatientRequest {

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 5, max = 20, message = "Phone number must be between 5 and 20 characters")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String nickname;
}