package com.example.demo.controller.patient;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.service.visit.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/patient/me/visits")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PatientVisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @PutMapping("/reschedule/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(
            @PathVariable UUID visitId,
            @RequestParam String newTime) {

        var visit = visitService.rescheduleVisit(
                visitId,
                LocalDateTime.parse(newTime)
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit rescheduled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @DeleteMapping("/cancel/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(
            @PathVariable UUID visitId) {

        var visit = visitService.cancelVisit(visitId);

        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit cancelled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> searchMyVisits(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end
    ) {
        var visits = visitService.searchVisitsForAuthenticatedPatient(
                doctorId, start, end
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Patient visits fetched successfully",
                        visitMapper.toResponseList(visits))
        );
    }
}