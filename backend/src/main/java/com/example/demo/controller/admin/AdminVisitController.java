package com.example.demo.controller.admin;

import com.example.demo.dto.request.visit.CreateVisitRequest;
import com.example.demo.dto.request.visit.UpdateVisitRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.mapper.VisitMapper;
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
@RequestMapping("${api.prefix}/admin/visits")
@RequiredArgsConstructor
public class AdminVisitController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<VisitResponse>> createVisit(
            @Valid @RequestBody CreateVisitRequest request) {

        var created = visitService.createVisit(
                visitMapper.fromCreateRequest(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        201,
                        "Visit created successfully",
                        visitMapper.toResponse(created)
                ));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> updateVisit(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVisitRequest request) {

        var updated = visitService.updateVisit(
                visitMapper.toUpdateEntity(id, request)
        );

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visit updated successfully",
                        visitMapper.toResponse(updated)
                )
        );
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getVisitById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visit fetched successfully",
                        visitMapper.toResponse(visitService.getById(id))
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteVisit(@PathVariable UUID id) {
        visitService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit deleted successfully", null)
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> searchVisits(
            @RequestParam(required = false) UUID doctorId,
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) VisitStatus status,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end
    ) {
        var visits = visitService.searchVisitsForAdmin(
                doctorId, patientId, status, start, end
        );

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visits fetched successfully",
                        visitMapper.toResponseList(visits)
                )
        );
    }
}