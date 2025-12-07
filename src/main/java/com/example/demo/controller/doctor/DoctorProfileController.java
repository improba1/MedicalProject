package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.service.doctor.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/doctor/profile")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorService doctorService;

    // 🔹 Подивитись свій профіль
    @GetMapping
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getProfile() {
        DoctorResponse response = doctorService.getCurrentDoctorProfile();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor profile fetched successfully", response));
    }

    // 🔹 Оновити профіль
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateProfile(@RequestBody UpdateDoctorRequest request) {
        DoctorResponse response = doctorService.updateDoctorProfile(request);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor profile updated successfully", response));
    }

    // 🔹 Видалити профіль (з автоматичним logout)
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(HttpServletRequest request, HttpServletResponse response) {
        doctorService.deleteDoctorProfile(request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor profile deleted and logged out successfully", null));
    }
}