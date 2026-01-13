package com.example.demo.controller.doctor;


import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.service.doctor.DoctorService;
import com.example.demo.service.patient.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctor/patients")
@RequiredArgsConstructor
public class DoctorPatientController {

    private final DoctorService doctorService;
    private final PatientMapper patientMapper;
    private final PatientService patientService;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(@PathVariable UUID id) {

        Patient patient = doctorService.getPatientIfDoctorHasAccess(id);
        PatientResponse dto = patientMapper.toResponse(patient);

        return ResponseEntity.ok(
                ApiResponse.of(200, "Patient fetched successfully", dto)
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<List<PatientResponse>> searchMyPatients(
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(
                patientMapper.toResponseList(
                        patientService.searchPatientsForCurrentDoctor(name)
                )
        );
    }
}

