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

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(
            @Valid @RequestBody AddDoctorRequest request) {

        Doctor doctor = doctorMapper.toEntity(request);
        Doctor saved = doctorService.create(doctor);
        DoctorResponse response = doctorMapper.toResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(),
                        "Doctor created successfully", response));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID id,
            @ModelAttribute UpdateDoctorRequest request
    ) {
        Doctor existing = doctorService.getById(id);
        Doctor mapped = doctorMapper.toUpdatedEntity(existing, request);
        Doctor saved = doctorService.updateDoctorWithImage(mapped, request.getImage());
        DoctorResponse response = doctorMapper.toResponse(saved);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Doctor updated successfully",
                response
        ));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable UUID id) {

        Doctor doctor = doctorService.getById(id);
        DoctorResponse response = doctorMapper.toResponse(doctor);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor fetched successfully", response));
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> softDeleteDoctor(@PathVariable UUID id) {
        Doctor doctor = doctorService.getById(id);
        doctor.setActive(false);

        Doctor updated = doctorService.update(doctor);
        DoctorResponse response = doctorMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor deactivated successfully", response));
    }

    @DeleteMapping("/hard-delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> hardDeleteDoctor(@PathVariable UUID id) {

        doctorService.delete(id);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor permanently deleted", null));
    }
}