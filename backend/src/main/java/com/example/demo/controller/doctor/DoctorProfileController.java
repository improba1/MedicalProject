package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // 🔹 Подивитись свій профіль
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getProfile() {
        Doctor doctor = doctorService.getCurrentDoctor();
        DoctorResponse response = doctorMapper.toResponse(doctor);
        return ResponseEntity.ok(
                ApiResponse.of(HttpStatus.OK.value(),
                        "Doctor profile fetched successfully", response)
        );
    }

    // 🔹 Оновити профіль
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateProfile(
            @ModelAttribute UpdateDoctorRequest request
    ) {
        Doctor current = doctorService.getCurrentDoctor();
        Doctor mapped = doctorMapper.toUpdatedEntity(current, request);
        Doctor saved = doctorService.updateOwnProfileWithImage(mapped, request.getImage());
        DoctorResponse response = doctorMapper.toResponse(saved);
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Doctor profile updated successfully",
                        response
                )
        );
    }

    // 🔹 Видалити профіль (soft delete + logout)
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(HttpServletRequest request, HttpServletResponse response) {
        doctorService.deactivateDoctorProfile(request, response);
        return ResponseEntity
                .ok(ApiResponse.of(HttpStatus.OK.value(),
                        "Doctor profile deleted and logged out successfully", null));
    }
}