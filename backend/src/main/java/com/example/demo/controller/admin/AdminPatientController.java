package com.example.demo.controller.admin;

import com.example.demo.dto.request.patient.PatientRegisterRequest;
import com.example.demo.dto.request.patient.PatientUpdateRequest;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/patients")
@RequiredArgsConstructor
public class AdminPatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody PatientRegisterRequest request
    ) {
        Patient entity = patientMapper.toEntity(request);
        Patient saved = patientService.create(entity);
        PatientResponse dto = patientMapper.toResponse(saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Patient created successfully", dto));
    }

    @PutMapping("/update/{patientId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable UUID patientId,
            @RequestBody PatientUpdateRequest request
    ) {
        Patient patient = patientService.getById(patientId);
        patientMapper.updateEntity(patient, request);
        Patient updated = patientService.update(patient);
        PatientResponse dto = patientMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient updated successfully", dto));
    }

    @GetMapping("/get/{patientId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable UUID patientId) {
        Patient patient = patientService.getById(patientId);
        PatientResponse dto = patientMapper.toResponse(patient);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient fetched successfully", dto));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<PatientResponse>> searchPatients(
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(
                patientMapper.toResponseList(
                        patientService.searchPatientsForAdmin(name)
                )
        );
    }

    @PutMapping("/deactivate/{patientId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<PatientResponse>> softDeletePatient(
            @PathVariable UUID patientId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Patient deactivated = patientService.deactivatePatientById(patientId, request, response);
        PatientResponse dto = patientMapper.toResponse(deactivated);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient deactivated successfully", dto));
    }

    @DeleteMapping("/hard-delete/{patientId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> hardDeletePatient(
            @PathVariable UUID patientId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        patientService.delete(patientId, request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Patient permanently deleted", null));
    }
}