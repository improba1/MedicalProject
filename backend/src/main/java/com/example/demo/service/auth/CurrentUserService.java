package com.example.demo.service.auth;

import com.example.demo.model.Doctor;
import com.example.demo.model.User;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserService {


    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;


    public User getAuthenticatedUser() {
        String email = getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));
    }


    public String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
        return auth.getName();
    }


    public UUID getUserId() {
        String email = getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }


    public UUID getPatientId() {
        return getUserId(); // якщо Patient = User
    }


    public UUID getDoctorId() {
        String email = getEmail();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"))
                .getId();
    }


    public Doctor getAuthenticatedDoctor() {
        String email = getEmail();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }
}