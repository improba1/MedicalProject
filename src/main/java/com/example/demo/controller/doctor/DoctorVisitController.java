package com.example.demo.controller.doctor;

import com.example.demo.dto.request.visit.UpdateVisitRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.model.User;
import com.example.demo.service.doctor.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctors/me/visits")
@RequiredArgsConstructor
public class DoctorVisitController {

    private final DoctorService doctorService;
    private final VisitMapper visitMapper;

    // 🔹 Отримати всі свої візити
    @GetMapping
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getMyVisits(@AuthenticationPrincipal User user) {
        var visits = doctorService.getVisits(user.getId());
        var responses = visitMapper.toResponseList(visits);
        return ResponseEntity.ok(
                ApiResponse.of(HttpStatus.OK.value(), "Doctor visits fetched successfully", responses)
        );
    }

    // 🔹 Скасувати свій візит
    @PutMapping("/{visitId}/cancel")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelMyVisit(@AuthenticationPrincipal User user,
                                                                    @PathVariable UUID visitId) {
        var visit = doctorService.cancelVisit(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(HttpStatus.OK.value(), "Visit cancelled successfully", visitMapper.toResponse(visit))
        );
    }

    // 🔹 Перенести свій візит
    @PutMapping("/{visitId}/reschedule")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleMyVisit(@AuthenticationPrincipal User user,
                                                                        @PathVariable UUID visitId,
                                                                        @Valid @RequestBody UpdateVisitRequest request) {
        var visit = doctorService.rescheduleVisit(visitId, request.getNewAppointmentTime());
        return ResponseEntity.ok(
                ApiResponse.of(HttpStatus.OK.value(), "Visit rescheduled successfully", visitMapper.toResponse(visit))
        );
    }

    // 🔹 Отримати конкретний візит
    @GetMapping("/{visitId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getMyVisitById(@AuthenticationPrincipal User user,
                                                                     @PathVariable UUID visitId) {
        var visit = doctorService.getVisits(user.getId())
                .stream()
                .filter(v -> v.getId().equals(visitId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        return ResponseEntity.ok(
                ApiResponse.of(HttpStatus.OK.value(), "Doctor visit fetched successfully", visitMapper.toResponse(visit))
        );
    }
}