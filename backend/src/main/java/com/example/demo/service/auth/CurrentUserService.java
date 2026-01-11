package com.example.demo.service.auth;

import com.example.demo.model.Doctor;
import com.example.demo.model.User;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public User getAuthenticatedUser() {
        return userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));
    }

    public String getEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public UUID getUserId() {
        return userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }

    public UUID getPatientId() {
        return getUserId(); // якщо Patient = User
    }

    public UUID getDoctorId() {
        return doctorRepository.findByEmail(getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"))
                .getId();
    }

    public Doctor getAuthenticatedDoctor() {
        return doctorRepository.findByEmail(getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }
}