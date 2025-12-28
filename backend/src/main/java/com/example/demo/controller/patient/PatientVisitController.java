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

    // 🔹 Перенести запис
    @PutMapping("/reschedule/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(
            @PathVariable UUID visitId,
            @RequestParam String newTime) {

        var updated = visitService.rescheduleVisit(visitId, LocalDateTime.parse(newTime));
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit rescheduled successfully",
                        visitMapper.toResponse(updated))
        );
    }

    // 🔹 Відмовитись від запису
    @DeleteMapping("/cancel/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(@PathVariable UUID visitId) {
        var canceled = visitService.cancelVisit(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit cancelled successfully",
                        visitMapper.toResponse(canceled))
        );
    }

    // 🔹 Отримати всі свої візити
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getAllVisits() {
        var visits = visitService.getUserVisits();
        return ResponseEntity.ok(
                ApiResponse.of(200, "User visits fetched successfully",
                        visitMapper.toResponseList(visits))
        );
    }

    // 🔹 Отримати всі майбутні візити
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getUpcomingVisits() {
        var visits = visitService.getUpcomingUserVisits();
        return ResponseEntity.ok(
                ApiResponse.of(200, "Upcoming visits fetched successfully",
                        visitMapper.toResponseList(visits))
        );
    }
}