package com.example.demo.controller.admin;

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

    // ------------------ CREATE ------------------
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<RaportResponse>> createRaport(@Valid @RequestBody Raport raport) {
        Raport created = raportService.create(raport);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Raport created successfully",
                        raportMapper.toResponse(created)));
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<RaportResponse>> updateRaport(
            @PathVariable UUID id,
            @Valid @RequestBody Raport raport) {

        Raport updated = raportService.update(id, raport);
        return okOne(updated, "Raport updated successfully");
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

    // ------------------ GET BY DOCTOR ------------------
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getRaportsByDoctor(@PathVariable UUID doctorId) {
        return okList(raportService.getByDoctorId(doctorId), "Raports fetched successfully by doctor");
    }

    // ------------------ GET BY PATIENT ------------------
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getRaportsByPatient(@PathVariable UUID patientId) {
        return okList(raportService.getByPatientId(patientId), "Raports fetched successfully by patient");
    }

    // ------------------ GET BY VISIT ------------------
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(@PathVariable UUID visitId) {
        return okOne(raportService.getByVisitId(visitId), "Raport fetched successfully by visit");
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteRaport(@PathVariable UUID id) {
        raportService.delete(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport deleted successfully", null));
    }

    // ------------------ GET BY DATE RANGE ------------------
    @GetMapping("/date-range/{start}/{end}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByDateRange(
            @PathVariable String start,
            @PathVariable String end) {

        return okList(
                raportService.getByDateRange(LocalDateTime.parse(start), LocalDateTime.parse(end)),
                "Raports fetched successfully by date range"
        );
    }

    // ------------------ GET BY DOCTOR + DATE RANGE ------------------
    @GetMapping("/doctor/{doctorId}/date-range/{start}/{end}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByDoctorAndDate(
            @PathVariable UUID doctorId,
            @PathVariable String start,
            @PathVariable String end) {

        return okList(
                raportService.getByDoctorAndDate(doctorId, LocalDateTime.parse(start), LocalDateTime.parse(end)),
                "Raports fetched successfully by doctor and date range"
        );
    }

    // ------------------ GET BY PATIENT + DATE RANGE ------------------
    @GetMapping("/patient/{patientId}/date-range/{start}/{end}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByPatientAndDate(
            @PathVariable UUID patientId,
            @PathVariable String start,
            @PathVariable String end) {

        return okList(
                raportService.getByPatientAndDate(patientId, LocalDateTime.parse(start), LocalDateTime.parse(end)),
                "Raports fetched successfully by patient and date range"
        );
    }

    // ------------------ GET BY VISIT + DOCTOR ------------------
    @GetMapping("/visit/{visitId}/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByVisitAndDoctor(
            @PathVariable UUID visitId,
            @PathVariable UUID doctorId) {

        return okList(
                raportService.getByVisitAndDoctor(visitId, doctorId),
                "Raports fetched successfully by visit and doctor"
        );
    }

    // ------------------ GET BY VISIT + PATIENT ------------------
    @GetMapping("/visit/{visitId}/patient/{patientId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getByVisitAndPatient(
            @PathVariable UUID visitId,
            @PathVariable UUID patientId) {

        return okList(
                raportService.getByVisitAndPatient(visitId, patientId),
                "Raports fetched successfully by visit and patient"
        );
    }
}