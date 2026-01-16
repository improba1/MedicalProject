package com.example.demo.controller.doctor;

import com.example.demo.dto.request.medical_service.MedicalServiceCreateRequest;
import com.example.demo.dto.request.medical_service.MedicalServiceSearchRequest;
import com.example.demo.dto.request.medical_service.MedicalServiceUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.MedicalServiceResponse;
import com.example.demo.mapper.MedicalServiceMapper;
import com.example.demo.model.MedicalService;
import com.example.demo.service.medical_service.MedicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctor/medical-services")
@RequiredArgsConstructor
public class DoctorMedicalServiceController {

    private final MedicalServiceService medicalServiceService;
    private final MedicalServiceMapper medicalServiceMapper;

    @GetMapping("/get/{medicalServiceId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<MedicalServiceResponse>> getMyMedicalServiceById(@PathVariable UUID medicalServiceId) {
        var service = medicalServiceService.getByIdForAuthenticatedDoctor(medicalServiceId);
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Medical service retrieved successfully",
                        medicalServiceMapper.toResponse(service)
                )
        );
    }


    @PostMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<MedicalServiceResponse>>> searchOwn(
            @RequestBody MedicalServiceSearchRequest req
    ) {
        var services = medicalServiceService.searchForAuthenticatedDoctor(
                req.getName(),
                req.getActive(),
                req.getMinPrice(),
                req.getMaxPrice()
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Services fetched successfully", medicalServiceMapper.toResponseList(services))
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('doctor:create')")
    public MedicalServiceResponse create(@RequestBody MedicalServiceCreateRequest request) {
        MedicalService entity = medicalServiceMapper.fromCreateRequest(request);
        MedicalService saved = medicalServiceService.createForAuthenticatedDoctor(entity);
        return medicalServiceMapper.toResponse(saved);
    }

    @PutMapping("/update/{medicalServiceId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public MedicalServiceResponse update(
            @PathVariable UUID medicalServiceId,
            @RequestBody MedicalServiceUpdateRequest request
    ) {
        MedicalService existing = medicalServiceService.getById(medicalServiceId);
        medicalServiceMapper.toUpdateEntity(existing, request);
        MedicalService updated = medicalServiceService.updateForAuthenticatedDoctor(existing);
        return medicalServiceMapper.toResponse(updated);
    }

    @DeleteMapping("/delete/{medicalServiceId}")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public void delete(@PathVariable UUID medicalServiceId) {
        medicalServiceService.deleteForAuthenticatedDoctor(medicalServiceId);
    }
}