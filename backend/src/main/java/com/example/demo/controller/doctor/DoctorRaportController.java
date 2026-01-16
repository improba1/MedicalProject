package com.example.demo.controller.doctor;

import com.example.demo.dto.request.raport.RaportSearchRequest;
import com.example.demo.dto.request.raport.RaportUpdateRequest;
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

    @GetMapping("/get/{raportId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getMyRaportById(
            @PathVariable UUID raportId
    ) {
        var raport = raportService.getRaportByIdForAuthenticatedDoctor(raportId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Raport retrieved successfully",
                        raportMapper.toResponse(raport)
                )
        );
    }

    @PutMapping("/update/{raportId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<RaportResponse>> updateRaport(
            @PathVariable UUID raportId,
            @Valid @RequestBody RaportUpdateRequest request) {
        Raport updated = raportService.update(raportId, raportMapper.updateEntity(raportService.getRaportById(raportId), request));
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Raport updated successfully",
                        raportMapper.toResponse(updated)
                )
        );
    }


    @PostMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> searchRaportsDoctor(
            @RequestBody RaportSearchRequest r
    ) {
        var raports = raportService.searchForDoctor(
                r.getVisitId(),
                r.getPatientId(),
                r.getDisease(),
                r.getPaymentReceipt(),
                r.getMinPrice(),
                r.getMaxPrice(),
                r.getFrom(),
                r.getTo()
        );
        return ResponseEntity.ok(
                ApiResponse.of(200, "Raports fetched", raportMapper.toResponseList(raports))
        );
    }
}