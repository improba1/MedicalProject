package com.example.demo.controller.doctor;

import com.example.demo.dto.request.visit.UpdateVisitRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.service.visit.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctors/me/visits")
@RequiredArgsConstructor
public class DoctorVisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @PutMapping("/{visitId}/cancel")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(@PathVariable UUID visitId) {
        var visit = visitService.cancelVisitByDoctor(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit cancelled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @PutMapping("/{visitId}/reschedule")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(
            @PathVariable UUID visitId,
            @Valid @RequestBody UpdateVisitRequest request) {

        var visit = visitService.rescheduleVisitByDoctor(
                visitId,
                request.getNewAppointmentTime()
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit rescheduled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> searchMyVisits(
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end
    ) {
        var visits = visitService.searchVisitsForAuthenticatedDoctor(
                patientId, start, end
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Doctor visits fetched successfully",
                        visitMapper.toResponseList(visits))
        );
    }
}