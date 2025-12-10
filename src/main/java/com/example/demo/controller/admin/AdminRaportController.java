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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/raports")
@RequiredArgsConstructor
public class AdminRaportController {

    private final RaportService raportService;
    private final RaportMapper raportMapper;

    // CREATE raport
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<RaportResponse>> createRaport(@Valid @RequestBody Raport raport) {
        Raport created = raportService.create(raport);
        RaportResponse response = raportMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Raport created successfully", response));
    }

    // UPDATE raport
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<RaportResponse>> updateRaport(@PathVariable UUID id,
                                                                    @Valid @RequestBody Raport raport) {
        Raport updated = raportService.update(id, raport);
        RaportResponse response = raportMapper.toResponse(updated);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport updated successfully", response));
    }

    // GET raport by id
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportById(@PathVariable UUID id) {
        Raport raport = raportService.getById(id);
        RaportResponse response = raportMapper.toResponse(raport);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport fetched successfully", response));
    }

    // GET all raports
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getAllRaports() {
        List<Raport> raports = raportService.getAll();
        List<RaportResponse> responses = raportMapper.toResponseList(raports);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "All raports fetched successfully", responses));
    }

    // GET raports by doctor id
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getRaportsByDoctor(@PathVariable UUID doctorId) {
        List<Raport> raports = raportService.getByDoctorId(doctorId);
        List<RaportResponse> responses = raportMapper.toResponseList(raports);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raports fetched successfully by doctor", responses));
    }

    // GET raports by patient id
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> getRaportsByPatient(@PathVariable UUID patientId) {
        List<Raport> raports = raportService.getByPatientId(patientId);
        List<RaportResponse> responses = raportMapper.toResponseList(raports);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raports fetched successfully by patient", responses));
    }

    // GET raport by visit id
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getRaportByVisit(@PathVariable UUID visitId) {
        Raport raport = raportService.getByVisitId(visitId);
        RaportResponse response = raportMapper.toResponse(raport);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport fetched successfully by visit", response));
    }

    // DELETE raport
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteRaport(@PathVariable UUID id) {
        raportService.delete(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Raport deleted successfully", null));
    }
}