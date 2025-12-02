package com.example.demo.dto.request.user;

import com.example.demo.enums.Role;
import com.example.demo.enums.Sex;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String nickname;

    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank(message = "Lastname is required")
    @Size(min = 2, max = 50)
    private String lastname;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotNull(message = "Sex is required")
    private Sex sex;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\+?[0-9]{9,15}", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100)
    private String password;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
}