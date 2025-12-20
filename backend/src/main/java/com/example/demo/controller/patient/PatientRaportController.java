package com.example.demo.controller.patient;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.mapper.RaportMapper;
import com.example.demo.model.Raport;
import com.example.demo.service.raport.RaportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/patient/me/raports")
@RequiredArgsConstructor
public class PatientRaportController {

    private final RaportService raportService;
    private final RaportMapper raportMapper;

    // 🔹 Отримати рапорт за візитом (тільки свій)
    @GetMapping("/get-by-visit-id/{visitId}")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(@PathVariable UUID visitId) {
        Raport raport = raportService.getRaportByVisitForUser(visitId);
        RaportResponse response = raportMapper.toResponse(raport);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport fetched successfully", response));
    }

    // 🔹 Отримати всі свої рапорти
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getAllRaports() {
        List<Raport> raports = raportService.getUserRaports();
        List<RaportResponse> responses = raportMapper.toResponseList(raports);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User raports fetched successfully", responses));
    }
}