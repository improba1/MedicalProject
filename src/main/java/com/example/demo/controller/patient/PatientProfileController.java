package com.example.demo.controller.patient;

import com.example.demo.dto.request.patient.UpdatePatientRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.service.patient.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/patient/me/profile")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    // 🔹 Подивитись свій профіль
    @GetMapping
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<PatientResponse>> getProfile() {
        Patient patient = patientService.getCurrentPatient();
        PatientResponse response = patientMapper.toResponse(patient);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Patient profile fetched successfully", response));
    }

    // 🔹 Оновити профіль
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('patient:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> updateProfile(@RequestBody UpdatePatientRequest request) {
        Patient currentPatient = patientService.getAuthenticatedPatient();
        patientMapper.updateEntity(currentPatient, request);
        Patient updated = patientService.updateCurrentPatient(currentPatient);
        PatientResponse response = patientMapper.toResponse(updated);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Patient profile updated successfully", response));
    }

    // 🔹 Видалити профіль (з автоматичним logout)
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('patient:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(HttpServletRequest request, HttpServletResponse response) {
        patientService.deletePatientProfile(request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Patient profile deleted and logged out successfully", null));
    }
}