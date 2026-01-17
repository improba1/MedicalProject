package com.example.demo.controller.admin;

import com.example.demo.dto.request.visit.VisitCreateByAdminRequest;
import com.example.demo.dto.request.visit.VisitUpdateRequest;
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
    public ResponseEntity<ApiResponse<VisitResponse>> createByAdmin(
            @Valid @RequestBody VisitCreateByAdminRequest request
    ) {

        Visit visit = visitMapper.fromAdminCreateRequest(request);
        Visit created = visitService.createByAdmin(visit);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        201,
                        "Visit created successfully",
                        visitMapper.toResponse(created)
                ));
    }


    @PutMapping("/update/{visitId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<VisitResponse>> updateVisit(
            @PathVariable UUID visitId,
            @Valid @RequestBody VisitUpdateRequest request) {

        var updated = visitService.updateVisit(
                visitMapper.toUpdateEntity(visitId, request)
        );

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visit updated successfully",
                        visitMapper.toResponse(updated)
                )
        );
    }

    @GetMapping("/get/{visitId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<VisitResponse>> getVisitById(@PathVariable UUID visitId) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Visit fetched successfully",
                        visitMapper.toResponse(visitService.getById(visitId))
                )
        );
    }

    @DeleteMapping("/delete/{visitId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteVisit(@PathVariable UUID visitId) {
        visitService.delete(visitId);
        return ResponseEntity.ok(
                ApiResponse.of(200, "Visit deleted successfully", null)
        );
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> searchVisitsForAdmin(
            @RequestBody VisitSearchRequest request
    ) {
        var visits = visitService.searchForAdmin(
                request.getDoctorId(),
                request.getPatientId(),
                request.getStatus(),
                request.getStart(),
                request.getEnd()
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