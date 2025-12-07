package com.example.demo.service.auth;

import com.example.demo.dto.request.AuthRequest;
import com.example.demo.dto.request.patient.RegisterPatientRequest;
import com.example.demo.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthResponse registerPatient(RegisterPatientRequest request);
    AuthResponse authenticate(AuthRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}