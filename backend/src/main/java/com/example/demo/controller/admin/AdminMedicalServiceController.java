package com.example.demo.controller.admin;

import com.example.demo.dto.request.medical_service.MedicalServiceCreateRequest;
import com.example.demo.dto.request.medical_service.MedicalServiceSearchRequest;
import com.example.demo.dto.request.medical_service.MedicalServiceUpdateRequest;
import com.example.demo.dto.response.MedicalServiceResponse;
import com.example.demo.mapper.MedicalServiceMapper;
import com.example.demo.model.MedicalService;
import com.example.demo.service.medical_service.MedicalServiceService;
import lombok.RequiredArgsConstructor;
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

    // ------------------ SEARCH ------------------
    @GetMapping("/{doctorId}/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<MedicalServiceResponse> search(@PathVariable UUID doctorId, @RequestBody MedicalServiceSearchRequest request) {
        return medicalServiceService.search(doctorId, request).stream()
                .map(medicalServiceMapper::toResponse)
                .toList();
    }

    // ------------------ CREATE ------------------
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

    // ------------------ UPDATE ------------------
    @PutMapping("/update/{id}/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public MedicalServiceResponse updateForDoctor(
            @PathVariable UUID id,
            @PathVariable UUID doctorId,
            @RequestBody MedicalServiceUpdateRequest request
    ) {
        MedicalService existing = medicalServiceService.getById(id);
        medicalServiceMapper.toUpdateEntity(existing, request);
        MedicalService updated = medicalServiceService.updateForDoctor(doctorId, existing);
        return medicalServiceMapper.toResponse(updated);
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void delete(@PathVariable UUID id) {
        medicalServiceService.delete(id);
    }
}