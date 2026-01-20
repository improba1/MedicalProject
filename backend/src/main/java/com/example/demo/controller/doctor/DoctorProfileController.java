package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor.DoctorUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.doctor.DoctorService;
import com.example.demo.service.logout.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/doctor/me/profile")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final LogoutService logoutService;
    private final CurrentUserService currentUserService;

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getProfile() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "Profile fetched", doctorMapper.toResponse(doctor)));
    }

    @PutMapping(value = "/update", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateProfile(@ModelAttribute DoctorUpdateRequest request) {
        Doctor updated = doctorMapper.toUpdatedEntity(new Doctor(), request);
        Doctor saved = doctorService.updateCurrentDoctor(updated, request.getImage());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "Profile updated", doctorMapper.toResponse(saved)));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteProfile() {
        doctorService.deactivateCurrentDoctor();
        logoutService.logoutCurrentUser();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK.value(), "Profile deactivated", null));
    }
}