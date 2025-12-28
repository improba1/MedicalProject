package com.example.demo.controller.doctor;

import com.example.demo.dto.request.visit.UpdateVisitRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.service.doctor.DoctorVisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctors/me/visits")
@RequiredArgsConstructor
public class DoctorVisitController {

    private final DoctorVisitService doctorVisitService;
    private final VisitMapper visitMapper;

    // 🔹 Отримати всі свої візити
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getMyVisits() {
        var visits = doctorVisitService.getOwnVisits();
        return ResponseEntity.ok(
                ApiResponse.of(200, "Doctor visits fetched successfully",
                        visitMapper.toResponseList(visits))
        );
    }

    // 🔹 Скасувати свій візит
    @PutMapping("/cancel/{visitId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelMyVisit(@PathVariable UUID visitId) {
        var visit = doctorVisitService.cancelOwnVisit(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit cancelled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    // 🔹 Перенести свій візит
    @PutMapping("/reschedule/{visitId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleMyVisit(
            @PathVariable UUID visitId,
            @Valid @RequestBody UpdateVisitRequest request) {

        var visit = doctorVisitService.rescheduleOwnVisit(visitId, request.getNewAppointmentTime());
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit rescheduled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    // 🔹 Отримати конкретний свій візит
    @GetMapping("/get/{visitId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getMyVisitById(@PathVariable UUID visitId) {
        var visit = doctorVisitService.getOwnVisitById(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Doctor visit fetched successfully",
                        visitMapper.toResponse(visit))
        );
    }
}