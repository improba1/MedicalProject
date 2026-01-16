package com.example.demo.controller.admin;

import com.example.demo.dto.request.disease.DiseaseCreateRequest;
import com.example.demo.dto.request.disease.DiseaseUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DiseaseResponse;
import com.example.demo.mapper.DiseaseMapper;
import com.example.demo.model.Disease;
import com.example.demo.service.disease.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/diseases")
@RequiredArgsConstructor
public class AdminDiseaseController {

    private final DiseaseService diseaseService;
    private final DiseaseMapper diseaseMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> createDisease(@Valid @RequestBody DiseaseCreateRequest request) {
        Disease disease = diseaseMapper.toEntity(request);
        Disease saved = diseaseService.addDisease(disease);
        DiseaseResponse response = diseaseMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Disease created successfully", response));
    }

    @PutMapping("/update/{diseaseId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> updateDisease(@PathVariable UUID diseaseId,
                                                                      @RequestBody DiseaseUpdateRequest request) {
        Disease disease = diseaseService.getById(diseaseId);
        diseaseMapper.updateEntity(disease, request);
        Disease updated = diseaseService.update(disease);
        DiseaseResponse response = diseaseMapper.toResponse(updated);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Disease updated successfully", response));
    }

    @GetMapping("/get/{diseaseId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> getDiseaseById(@PathVariable UUID diseaseId) {
        Disease disease = diseaseService.getById(diseaseId);
        DiseaseResponse response = diseaseMapper.toResponse(disease);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Disease fetched successfully", response));
    }

    @DeleteMapping("/delete/{diseaseId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteDisease(@PathVariable UUID diseaseId) {
        diseaseService.deleteDisease(diseaseId);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Disease deleted successfully", null));
    }
}