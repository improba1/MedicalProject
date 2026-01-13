package com.example.demo.controller.doctor;

import com.example.demo.dto.request.raport.UpdateRaportRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.mapper.RaportMapper;
import com.example.demo.model.Raport;
import com.example.demo.service.raport.RaportService;
import jakarta.validation.Valid;
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

    @PutMapping("/update/{raportId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<RaportResponse>> updateRaport(
            @PathVariable UUID raportId,
            @Valid @RequestBody UpdateRaportRequest request) {

        Raport raport = raportService.getOwnRaportByVisit(raportId);

        Raport updated = raportService.update(raportId,
                raportMapper.updateEntity(raport, request));

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Raport updated successfully",
                raportMapper.toResponse(updated)
        ));
    }


    // 🔹 Отримати всі свої рапорти
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getAllMyRaports() {
        List<RaportResponse> responses =
                raportMapper.toResponseList(raportService.getDoctorRaports());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Doctor raports fetched successfully",
                responses
        ));
    }

    // 🔹 Отримати всі рапорти конкретного пацієнта (якого приймав даний лікар)
    @GetMapping("/get/patient/{patientId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getRaportsByPatient(
            @PathVariable UUID patientId) {

        List<RaportResponse> responses =
                raportMapper.toResponseList(raportService.getOwnRaportsByPatient(patientId));

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Raports for patient fetched successfully",
                responses
        ));
    }

    // 🔹 Отримати рапорт за конкретним візитом
    @GetMapping("/get/visit/{visitId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(
            @PathVariable UUID visitId) {

        Raport raport = raportService.getOwnRaportByVisit(visitId);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Raport fetched successfully",
                raportMapper.toResponse(raport)
        ));
    }
}