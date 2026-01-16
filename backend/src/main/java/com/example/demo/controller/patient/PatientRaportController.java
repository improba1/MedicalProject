package com.example.demo.controller.patient;

import com.example.demo.dto.request.raport.RaportSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.mapper.RaportMapper;
import com.example.demo.service.raport.RaportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/patient/me/raports")
@RequiredArgsConstructor
public class PatientRaportController {

    private final RaportService raportService;
    private final RaportMapper raportMapper;

    @GetMapping("/get/{raportId}")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<RaportResponse>> getMyRaportById(
            @PathVariable UUID raportId
    ) {
        var raport = raportService.getRaportByIdForAuthenticatedPatient(raportId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Raport retrieved successfully",
                        raportMapper.toResponse(raport)
                )
        );
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<List<RaportResponse>>> searchRaportsPatient(
            @RequestBody RaportSearchRequest r
    ) {
        var raports = raportService.searchForPatient(
                r.getVisitId(),
                r.getDoctorId(),
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