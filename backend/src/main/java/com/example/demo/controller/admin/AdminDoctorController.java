package com.example.demo.controller.admin;

import com.example.demo.dto.request.doctor.AddDoctorRequest;
import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(
            @Valid @RequestBody AddDoctorRequest request) {

        Doctor doctor = doctorMapper.toEntity(request);
        Doctor saved = doctorService.create(doctor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Doctor created",
                        doctorMapper.toResponse(saved)));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID id,
            @ModelAttribute UpdateDoctorRequest request
    ) {
        Doctor updated = doctorMapper.toUpdatedEntity(new Doctor(), request);
        Doctor saved = doctorService.updateDoctorByAdmin(id, updated, request.getImage());

        return ResponseEntity.ok(ApiResponse.of(
                200, "Doctor updated", doctorMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> deactivateDoctor(@PathVariable UUID id) {
        Doctor doctor = doctorService.deactivateDoctor(id);

        return ResponseEntity.ok(ApiResponse.of(
                200,
                "Doctor deactivated",
                doctorMapper.toResponse(doctor)
        ));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> activateDoctor(@PathVariable UUID id) {
        Doctor doctor = doctorService.activateDoctor(id);

        return ResponseEntity.ok(ApiResponse.of(
                200,
                "Doctor activated",
                doctorMapper.toResponse(doctor)
        ));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Doctor fetched successfully",
                        doctorMapper.toResponse(doctorService.getById(id))
                )
        );
    }

    @DeleteMapping("/hard-delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.ok(ApiResponse.of(200, "Doctor deleted", null));
    }
}