package com.example.demo.dto.request.user;

import com.example.demo.enums.Sex;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String nickname;

    @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
    private String firstname;

    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    private String lastname;

    private LocalDate birthDate;

    private Sex sex;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "\\+?[0-9]{9,15}", message = "Invalid phone number")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
}