package com.example.demo.controller.patient;

import com.example.demo.dto.request.patient.PatientUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.logout.LogoutService;
import com.example.demo.service.patient.PatientService;
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
    private final LogoutService logoutService;
    private final CurrentUserService currentUserService;

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<PatientResponse>> getProfile() {
        Patient patient = patientService.getCurrentPatient();

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Patient profile fetched successfully",
                patientMapper.toResponse(patient)
        ));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('patient:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> updateProfile(
            @RequestBody PatientUpdateRequest request
    ) {
        Patient patient = patientService.getCurrentPatient();
        patientMapper.updateEntity(patient, request);
        Patient updated = patientService.updateCurrentPatient(patient);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Patient profile updated successfully",
                patientMapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('patient:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteProfile() {
        patientService.deactivateCurrentPatient(currentUserService.getAuthenticatedPatient());
        logoutService.logoutCurrentUser();
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Patient profile deleted and logged out successfully",
                null
        ));
    }
}