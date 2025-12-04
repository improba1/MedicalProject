package com.example.demo.controller.public_;

import com.example.demo.dto.request.AuthRequest;
import com.example.demo.dto.request.patient.RegisterPatientRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.auth.AuthService;
import com.example.demo.service.logout.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final LogoutService logoutService;

    // Реєстрація доступна тільки для пацієнтів
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterPatientRequest request) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    // Логін доступний для всіх (адмін, лікар, пацієнт)
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    // Оновлення токена
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    // 🔹 Явний logout
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('user:logout')")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Logged out successfully", null));
    }
}