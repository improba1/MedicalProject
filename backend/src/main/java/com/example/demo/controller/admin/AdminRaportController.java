package com.example.demo.controller.admin;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/raports")
@RequiredArgsConstructor
public class AdminRaportController {

    private final RaportService raportService;
    private final RaportMapper raportMapper;

    // ------------------ HELPERS ------------------

    private ResponseEntity<ApiResponse<List<RaportResponse>>> okList(List<Raport> list, String message) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                message,
                raportMapper.toResponseList(list)
        ));
    }

    private ResponseEntity<ApiResponse<RaportResponse>> okOne(Raport raport, String message) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                message,
                raportMapper.toResponse(raport)
        ));
    }

    // ------------------ GET BY ID ------------------
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportById(@PathVariable UUID id) {
        return okOne(raportService.getById(id), "Raport fetched successfully");
    }

    // ------------------ GET ALL ------------------
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getAllRaports() {
        return okList(raportService.getAll(), "All raports fetched successfully");
    }

    // ------------------ GET BY VISIT ------------------
    @GetMapping("/get/visit/{visitId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(@PathVariable UUID visitId) {
        return okOne(raportService.getByVisitId(visitId), "Raport fetched successfully by visit");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<RaportResponse>> updateRaport(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRaportRequest request) {
        Raport raport = raportService.getById(id);
        Raport updated = raportService.update(id,
                raportMapper.updateEntity(raport, request));
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Raport updated successfully",
                raportMapper.toResponse(updated)
        ));
    }


    // ------------------ DELETE ------------------
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteRaport(@PathVariable UUID id) {
        raportService.delete(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport deleted successfully", null));
    }

    // ------------------ GET BY DATE RANGE ------------------
    @GetMapping("/get/date-range")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return okList(
                raportService.getByDateRange(start, end),
                "Raports fetched successfully by date range"
        );
    }

    // ------------------ GET BY DOCTOR + DATE RANGE ------------------
    @GetMapping("/get/doctor/{doctorId}/date-range")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByDoctorAndDate(
            @PathVariable UUID doctorId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return okList(
                raportService.getByDoctorAndDate(doctorId, start, end),
                "Raports fetched successfully by doctor and date range"
        );
    }

    // ------------------ GET BY PATIENT + DATE RANGE ------------------
    @GetMapping("/get/patient/{patientId}/date-range")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByPatientAndDate(
            @PathVariable UUID patientId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {

        return okList(
                raportService.getByPatientAndDate(patientId, start, end),
                "Raports fetched successfully by patient and date range"
        );
    }
}