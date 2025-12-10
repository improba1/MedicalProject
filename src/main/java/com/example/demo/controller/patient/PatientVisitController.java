package com.example.demo.controller.patient;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.model.Visit;
import com.example.demo.service.visit.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // 🔹 Записатись до лікаря
    @PostMapping("/book/{doctorId}")
    public ResponseEntity<ApiResponse<VisitResponse>> bookVisit(@PathVariable UUID doctorId,
                                                                @RequestParam String appointmentTime) {
        Visit visit = visitService.bookVisit(doctorId, LocalDateTime.parse(appointmentTime)); // сервіс → ентіті
        VisitResponse response = visitMapper.toResponse(visit);                               // ентіті → DTO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Visit booked successfully", response));
    }

    // 🔹 Змінити запис
    @PutMapping("/reschedule/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(@PathVariable UUID visitId,
                                                                      @RequestParam String newTime) {
        Visit updated = visitService.rescheduleVisit(visitId, LocalDateTime.parse(newTime)); // сервіс → ентіті
        VisitResponse response = visitMapper.toResponse(updated);                            // ентіті → DTO
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit rescheduled successfully", response));
    }

    // 🔹 Відмовитись від запису
    @DeleteMapping("/cancel/{visitId}")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(@PathVariable UUID visitId) {
        Visit canceled = visitService.cancelVisit(visitId);                 // сервіс → ентіті
        VisitResponse response = visitMapper.toResponse(canceled);          // ентіті → DTO
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit cancelled successfully", response));
    }

    // 🔹 Отримати всі свої візити
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getAllVisits() {
        List<Visit> visits = visitService.getUserVisits();                  // сервіс → ентіті
        List<VisitResponse> responses = visitMapper.toResponseList(visits); // ентіті → DTO
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User visits fetched successfully", responses));
    }

    // 🔹 Отримати всі майбутні візити
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getUpcomingVisits() {
        List<Visit> visits = visitService.getUpcomingUserVisits();          // сервіс → ентіті
        List<VisitResponse> responses = visitMapper.toResponseList(visits); // ентіті → DTO
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Upcoming visits fetched successfully", responses));
    }
}