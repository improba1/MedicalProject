package com.example.demo.controller.doctor;

import com.example.demo.dto.request.visit.VisitUpdateRequest;
import com.example.demo.dto.request.visit.VisitSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.mapper.RaportMapper;
import com.example.demo.mapper.VisitMapper;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;
import com.example.demo.service.visit.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctor/me/visits")
@RequiredArgsConstructor
public class DoctorVisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;
    private final RaportMapper raportMapper;

    @GetMapping("/get/{visitId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getMyVisitById(
            @PathVariable UUID visitId
    ) {
        var visit = visitService.getByIdForAuthenticatedDoctor(visitId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visit retrieved successfully",
                        visitMapper.toResponse(visit)
                )
        );
    }

    @PutMapping("/{visitId}/cancel")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(@PathVariable UUID visitId) {
        var visit = visitService.cancelVisitByDoctor(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit cancelled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @PutMapping("/{visitId}/reschedule")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(
            @PathVariable UUID visitId,
            @Valid @RequestBody VisitUpdateRequest request) {

        var visit = visitService.rescheduleVisitByDoctor(
                visitId,
                request.getNewAppointmentTime()
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit rescheduled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @PostMapping("/{visitId}/update/raport")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<RaportResponse>> updateRaport(
            @PathVariable UUID visitId,
            @RequestBody Raport raportUpdate
    ) {
        Raport raport = visitService.updateRaport(visitId, raportUpdate);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Raport updated", raportMapper.toResponse(raport))
        );
    }

    @PostMapping("/{visitId}/complete")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> completeVisit(
            @PathVariable UUID visitId
    ) {
        Visit visit = visitService.completeVisit(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit marked as completed", visitMapper.toResponse(visit))
        );
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> searchVisitsForDoctor(
            @RequestBody VisitSearchRequest request
    ) {
        var visits = visitService.searchForDoctor(
                request.getPatientId(),
                request.getStatus(),
                request.getStart(),
                request.getEnd()
        );

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Doctor visits fetched successfully",
                        visitMapper.toResponseList(visits)
                )
        );
    }
}