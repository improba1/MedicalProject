package com.example.demo.controller.patient;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.service.raport.RaportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/user/raports")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PatientRaportController {

    private final RaportService raportService;

    // 🔹 Отримати рапорт за візитом
    @GetMapping("/visit/{visitId}")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(@PathVariable UUID visitId) {
        RaportResponse response = raportService.getRaportByVisitForUser(visitId);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport fetched successfully", response));
    }

    // 🔹 Отримати всі свої рапорти
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getAllRaports() {
        List<RaportResponse> raports = raportService.getUserRaports();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User raports fetched successfully", raports));
    }
}