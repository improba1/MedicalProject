package com.example.demo.controller.doctor;

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
@RequestMapping("/api/doctor/medical-services")
@RequiredArgsConstructor
public class DoctorMedicalServiceController {

    private final MedicalServiceService medicalServiceService;
    private final MedicalServiceMapper medicalServiceMapper;

    // ==========================
    // 🔹 Пошук власних сервісів (без CurrentUserService у контролері)
    // ==========================
    @GetMapping
    @PreAuthorize("hasAuthority('doctor:read')")
    public List<MedicalServiceResponse> search(MedicalServiceSearchRequest request) {
        return medicalServiceService. searchForAuthenticatedDoctor(request).stream()
                .map(medicalServiceMapper::toResponse)
                .toList();
    }

    // ==========================
    // 🔹 Створення нового сервісу для залогованого лікаря
    // ==========================
    @PostMapping
    @PreAuthorize("hasAuthority('doctor:create')")
    public MedicalServiceResponse create(@RequestBody MedicalServiceCreateRequest request) {
        MedicalService entity = medicalServiceMapper.fromCreateRequest(request);
        MedicalService saved = medicalServiceService.createForAuthenticatedDoctor(entity);
        return medicalServiceMapper.toResponse(saved);
    }

    // ==========================
    // 🔹 Оновлення свого сервісу
    // ==========================
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public MedicalServiceResponse update(
            @PathVariable UUID id,
            @RequestBody MedicalServiceUpdateRequest request
    ) {
        MedicalService existing = medicalServiceService.getById(id);
        medicalServiceMapper.toUpdateEntity(existing, request);
        MedicalService updated = medicalServiceService.updateForAuthenticatedDoctor(existing);
        return medicalServiceMapper.toResponse(updated);
    }

    // ==========================
    // 🔹 Видалення свого сервісу
    // ==========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public void delete(@PathVariable UUID id) {
        medicalServiceService.deleteForAuthenticatedDoctor(id);
    }
}