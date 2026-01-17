package com.example.demo.controller.patient;

import com.example.demo.dto.request.visit.VisitCreateByPatientRequest;
import com.example.demo.dto.request.visit.VisitSearchRequest;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/patient/me/visits")
@RequiredArgsConstructor
public class PatientVisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping("/get/{visitId}")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getMyVisitById(
            @PathVariable UUID visitId
    ) {
        var visit = visitService.getByIdForAuthenticatedPatient(visitId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visit retrieved successfully",
                        visitMapper.toResponse(visit)
                )
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('patient:create')")
    public ResponseEntity<ApiResponse<VisitResponse>> createByPatient(
            @Valid @RequestBody VisitCreateByPatientRequest request
    ) {
        Visit visit = visitMapper.fromPatientCreateRequest(request);
        Visit created = visitService.createByPatient(visit);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        201,
                        "Visit created successfully",
                        visitMapper.toResponse(created)
                ));
    }



    @PutMapping("/reschedule/{visitId}")
    @PreAuthorize("hasAuthority('patient:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> rescheduleVisit(
            @PathVariable UUID visitId,
            @RequestParam String newTime) {

        var visit = visitService.rescheduleVisit(
                visitId,
                LocalDateTime.parse(newTime)
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit rescheduled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @PutMapping("/cancel/{visitId}")
    @PreAuthorize("hasAuthority('patient:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> cancelVisit(
            @PathVariable UUID visitId) {

        var visit = visitService.cancelVisit(visitId);

        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit cancelled successfully",
                        visitMapper.toResponse(visit))
        );
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> searchVisitsForPatient(
            @RequestBody VisitSearchRequest request
    ) {
        var visits = visitService.searchForPatient(
                request.getDoctorId(),
                request.getStatus(),
                request.getStart(),
                request.getEnd()
        );
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Patient visits fetched successfully",
                        visitMapper.toResponseList(visits)
                )
        );
    }
}