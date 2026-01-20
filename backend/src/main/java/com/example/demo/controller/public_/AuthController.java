package com.example.demo.controller.public_;

import com.example.demo.dto.request.auth.AuthRequest;
import com.example.demo.dto.request.patient.PatientRegisterRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.auth.AuthService;
import com.example.demo.service.logout.LogoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final LogoutService logoutService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody PatientRegisterRequest request
    ) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @Valid @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('user:logout')")
    public ResponseEntity<ApiResponse<Void>> logout() {
        logoutService.logoutCurrentUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Logged out successfully",
                        null
                ));
    }
}