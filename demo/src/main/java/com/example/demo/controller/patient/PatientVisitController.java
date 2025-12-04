package com.example.demo.controller.patient;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.service.visit.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/user/visits")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PatientVisitController {

    private final VisitService visitService;

    // 🔹 Записатись до лікаря
    @PostMapping("/book/{doctorId}")
    public ResponseEntity<ApiResponse<VisitResponse>> bookVisit(@PathVariable UUID doctorId,
                                                                @RequestParam String appointmentTime) {
        VisitResponse response = visitService.bookVisit(doctorId, appointmentTime);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Visit booked successfully", response));
    }

    // 🔹 Змінити запис
    @PutMapping("/reschedule/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(@PathVariable UUID visitId,
                                                                      @RequestParam String newTime) {
        VisitResponse response = visitService.rescheduleVisit(visitId, newTime);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit rescheduled successfully", response));
    }

    // 🔹 Відмовитись від запису
    @DeleteMapping("/cancel/{visitId}")
    public ResponseEntity<ApiResponse<Void>> cancelVisit(@PathVariable UUID visitId) {
        visitService.cancelVisit(visitId);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit cancelled successfully", null));
    }

    // 🔹 Отримати всі свої візити
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getAllVisits() {
        List<VisitResponse> visits = visitService.getUserVisits();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User visits fetched successfully", visits));
    }

    // 🔹 Отримати всі майбутні візити
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getUpcomingVisits() {
        List<VisitResponse> visits = visitService.getUpcomingUserVisits();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Upcoming visits fetched successfully", visits));
    }
}