package com.example.demo.controller.doctor;

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
@RequestMapping("${api.prefix}/doctor/me/raports")
@RequiredArgsConstructor
public class DoctorRaportController {

    private final RaportService raportService;
    private final RaportMapper raportMapper;

    // 🔹 Отримати всі свої рапорти
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getAllMyRaports() {
        List<Raport> raports = raportService.getDoctorRaports();
        List<RaportResponse> responses = raportMapper.toResponseList(raports);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor raports fetched successfully", responses));
    }

    // 🔹 Отримати всі рапорти конкретного пацієнта (якого приймав даний лікар)
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getRaportsByPatient(@PathVariable UUID patientId) {
        List<Raport> raports = raportService.getOwnRaportsByPatient(patientId);
        List<RaportResponse> responses = raportMapper.toResponseList(raports);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Raports for patient fetched successfully", responses));
    }

    // 🔹 Отримати рапорт за конкретним візитом
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(@PathVariable UUID visitId) {
        Raport raport = raportService.getOwnRaportByVisit(visitId);
        RaportResponse response = raportMapper.toResponse(raport);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Raport fetched successfully", response));
    }
}