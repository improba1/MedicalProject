package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor.DoctorUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/doctor/me/profile")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getProfile() {
        Doctor doctor = doctorService.getCurrentDoctor();
        return ResponseEntity.ok(ApiResponse.of(
                200, "Profile fetched",
                doctorMapper.toResponse(doctor)
        ));
    }

    @PutMapping(value = "/update", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateProfile(
            @ModelAttribute DoctorUpdateRequest request
    ) {
        Doctor updated = doctorMapper.toUpdatedEntity(new Doctor(), request);
        Doctor saved = doctorService.updateCurrentDoctor(updated, request.getImage());

        return ResponseEntity.ok(ApiResponse.of(
                200, "Profile updated",
                doctorMapper.toResponse(saved)
        ));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        doctorService.deactivateCurrentDoctor(request, response);
        return ResponseEntity.ok(ApiResponse.of(
                200, "Profile deactivated", null
        ));
    }
}