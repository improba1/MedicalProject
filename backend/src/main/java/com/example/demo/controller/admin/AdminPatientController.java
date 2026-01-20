package com.example.demo.controller.admin;

import com.example.demo.dto.request.patient.PatientRegisterRequest;
import com.example.demo.dto.request.patient.PatientUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.service.logout.LogoutService;
import com.example.demo.service.patient.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/patients")
@RequiredArgsConstructor
public class AdminPatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final LogoutService logoutService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody PatientRegisterRequest request
    ) {
        Patient entity = patientMapper.toEntity(request);
        Patient saved = patientService.create(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "Patient created successfully",
                        patientMapper.toResponse(saved)
                ));
    }

    @PutMapping("/update/{patientId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody PatientUpdateRequest request
    ) {
        Patient patient = patientService.getById(patientId);
        patientMapper.updateEntity(patient, request);
        Patient updated = patientService.update(patient);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Patient updated successfully",
                        patientMapper.toResponse(updated)
                ));
    }

    @GetMapping("/get/{patientId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable UUID patientId
    ) {
        Patient patient = patientService.getById(patientId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Patient fetched successfully",
                        patientMapper.toResponse(patient)
                ));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> searchPatients(
            @RequestParam(required = false) String name
    ) {
        List<Patient> patients = patientService.searchPatientsForAdmin(name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Patients fetched successfully",
                        patientMapper.toResponseList(patients)
                ));
    }

    @PutMapping("/deactivate/{patientId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> deactivatePatient(
            @PathVariable UUID patientId
    ) {
        Patient patient = patientService.deactivatePatientById(patientId);
        logoutService.forceLogoutUser(patient.getEmail());
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Patient deactivated successfully",
                patientMapper.toResponse(patient)
        ));
    }

    @DeleteMapping("/hard-delete/{patientId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> hardDeletePatient(@PathVariable UUID patientId) {
        Patient patient = patientService.getById(patientId);
        patientService.delete(patientId);
        logoutService.forceLogoutUser(patient.getEmail());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Patient permanently deleted",
                null
        ));
    }
}