package com.example.demo.controller.admin;

import com.example.demo.dto.request.patient.RegisterPatientRequest;
import com.example.demo.dto.request.patient.UpdatePatientRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.service.patient.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/patients")
@RequiredArgsConstructor
public class AdminPatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    // CREATE patient
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody RegisterPatientRequest request
    ) {
        Patient entity = patientMapper.toEntity(request);
        Patient saved = patientService.create(entity);
        PatientResponse dto = patientMapper.toResponse(saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Patient created successfully", dto));
    }

    // UPDATE patient
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable UUID id,
            @RequestBody UpdatePatientRequest request
    ) {
        Patient patient = patientService.getById(id);
        patientMapper.updateEntity(patient, request);
        Patient updated = patientService.update(patient);
        PatientResponse dto = patientMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient updated successfully", dto));
    }

    // GET patient by id
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable UUID id) {
        Patient patient = patientService.getById(id);
        PatientResponse dto = patientMapper.toResponse(patient);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient fetched successfully", dto));
    }

    // SOFT DELETE patient (deactivate)
    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> softDeletePatient(
            @PathVariable UUID id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Patient deactivated = patientService.deactivatePatientById(id, request, response);
        PatientResponse dto = patientMapper.toResponse(deactivated);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient deactivated successfully", dto));
    }

    // HARD DELETE patient
    @DeleteMapping("/hard-delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> hardDeletePatient(
            @PathVariable UUID id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        patientService.delete(id, request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient permanently deleted", null));
    }
}