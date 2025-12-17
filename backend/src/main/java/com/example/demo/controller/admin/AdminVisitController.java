package com.example.demo.controller.admin;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.model.Visit;
import com.example.demo.service.visit.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/visits")
@RequiredArgsConstructor
public class AdminVisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    // CREATE visit
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<VisitResponse>> createVisit(@Valid @RequestBody Visit visit) {
        Visit created = visitService.create(visit);
        VisitResponse response = visitMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Visit created successfully", response));
    }

    // UPDATE visit
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> updateVisit(@PathVariable UUID id,
                                                                  @Valid @RequestBody Visit visit) {
        visit.setId(id);
        Visit updated = visitService.update(visit);
        VisitResponse response = visitMapper.toResponse(updated);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit updated successfully", response));
    }

    // GET visit by id
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getVisitById(@PathVariable UUID id) {
        Visit visit = visitService.getById(id);
        VisitResponse response = visitMapper.toResponse(visit);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit fetched successfully", response));
    }

    // GET visits by doctor id
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getVisitsByDoctor(@PathVariable UUID doctorId) {
        List<Visit> visits = visitService.getByDoctor(doctorId);
        List<VisitResponse> responses = visitMapper.toResponseList(visits);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visits fetched successfully by doctor", responses));
    }

    // GET visits by patient id
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> getVisitsByPatient(@PathVariable UUID patientId) {
        List<Visit> visits = visitService.getByPatient(patientId);
        List<VisitResponse> responses = visitMapper.toResponseList(visits);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visits fetched successfully by patient", responses));
    }

    // DELETE visit
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteVisit(@PathVariable UUID id) {
        visitService.delete(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Visit deleted successfully", null));
    }
}