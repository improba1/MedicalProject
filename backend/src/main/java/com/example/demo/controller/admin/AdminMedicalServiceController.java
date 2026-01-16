package com.example.demo.controller.admin;

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
@RequestMapping("${api.prefix}/admin/doctors/medical-services")
@RequiredArgsConstructor
public class AdminMedicalServiceController {

    private final MedicalServiceService medicalServiceService;
    private final MedicalServiceMapper medicalServiceMapper;

    @GetMapping("/get{medicalServiceId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<MedicalServiceResponse>> getMedicalServiceById(@PathVariable UUID medicalServiceId) {
        var service = medicalServiceService.getById(medicalServiceId);
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Medical service retrieved successfully",
                        medicalServiceMapper.toResponse(service)
                )
        );
    }


    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<MedicalServiceResponse>>> searchAdmin(
            @RequestBody MedicalServiceSearchRequest req
    ) {
        var services = medicalServiceService.searchForAdmin(
                req.getDoctorId(),
                req.getName(),
                req.getActive(),
                req.getMinPrice(),
                req.getMaxPrice()
        );

        return ResponseEntity.ok(
                ApiResponse.of(200, "Services fetched successfully", medicalServiceMapper.toResponseList(services))
        );
    }

    @PostMapping("/create/{doctorId}")
    @PreAuthorize("hasAuthority('admin:create')")
    public MedicalServiceResponse createForDoctor(
            @PathVariable UUID doctorId,
            @RequestBody MedicalServiceCreateRequest request
    ) {
        MedicalService entity = medicalServiceMapper.fromCreateRequest(request);
        MedicalService saved = medicalServiceService.createForDoctor(doctorId, entity);
        return medicalServiceMapper.toResponse(saved);
    }

    @PutMapping("/update/{medicalServiceId}/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public MedicalServiceResponse updateForDoctor(
            @PathVariable UUID medicalServiceId,
            @PathVariable UUID doctorId,
            @RequestBody MedicalServiceUpdateRequest request
    ) {
        MedicalService existing = medicalServiceService.getById(medicalServiceId);
        medicalServiceMapper.toUpdateEntity(existing, request);
        MedicalService updated = medicalServiceService.updateForDoctor(doctorId, existing);
        return medicalServiceMapper.toResponse(updated);
    }

    @DeleteMapping("/delete/{medicalServiceId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void delete(@PathVariable UUID medicalServiceId) {
        medicalServiceService.delete(medicalServiceId);
    }
}