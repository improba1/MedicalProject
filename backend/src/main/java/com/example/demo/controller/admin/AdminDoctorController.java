package com.example.demo.controller.admin;

import com.example.demo.dto.request.doctor.DoctorCreateRequest;
import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.request.doctor.DoctorUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> searchAdmin(
            @RequestBody DoctorSearchRequest request
    ) {
        var doctors = doctorService.searchDoctors(
                request.getName(),
                request.getSpecialization(),
                request.getRating(),
                request.getIsActive()
        );

        var response = doctorMapper.toResponseList(doctors);

        return ResponseEntity.ok(
                ApiResponse.of(200, "Doctors fetched successfully", response)
        );
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(
            @Valid @ModelAttribute DoctorCreateRequest request
    ) {
        Doctor doctor = doctorMapper.toEntity(request);
        Doctor saved = doctorService.create(doctor, request.getImage());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Doctor created", doctorMapper.toResponse(saved)));
    }

    @PutMapping(value = "/update/{doctorId}", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID doctorId,
            @ModelAttribute DoctorUpdateRequest request
    ) {
        Doctor updated = doctorMapper.toUpdatedEntity(new Doctor(), request);
        Doctor saved = doctorService.updateDoctorByAdmin(doctorId, updated, request.getImage());

        return ResponseEntity.ok(ApiResponse.of(
                200, "Doctor updated", doctorMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{doctorId}/deactivate")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> deactivateDoctor(@PathVariable UUID doctorId) {
        Doctor doctor = doctorService.deactivateDoctor(doctorId);

        return ResponseEntity.ok(ApiResponse.of(
                200,
                "Doctor deactivated",
                doctorMapper.toResponse(doctor)
        ));
    }

    @PutMapping("/{doctorId}/activate")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> activateDoctor(@PathVariable UUID doctorId) {
        Doctor doctor = doctorService.activateDoctor(doctorId);

        return ResponseEntity.ok(ApiResponse.of(
                200,
                "Doctor activated",
                doctorMapper.toResponse(doctor)
        ));
    }

    @GetMapping("/get/{doctorId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Doctor fetched successfully",
                        doctorMapper.toResponse(doctorService.getById(doctorId))
                )
        );
    }

    @DeleteMapping("/hard-delete/{doctorId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID doctorId) {
        doctorService.delete(doctorId);
        return ResponseEntity.ok(ApiResponse.of(200, "Doctor deleted", null));
    }
}