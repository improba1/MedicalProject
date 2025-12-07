package com.example.demo.controller.admin;

import com.example.demo.dto.request.disease.AddDiseaseRequest;
import com.example.demo.dto.request.disease.UpdateDiseaseRequest;
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

    // CREATE disease
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> createDisease(@Valid @RequestBody AddDiseaseRequest request) {
        Disease disease = diseaseMapper.toEntity(request);
        Disease saved = diseaseService.addDisease(disease);
        DiseaseResponse response = diseaseMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Disease created successfully", response));
    }

    // UPDATE disease (тепер чисто через сервіс)
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> updateDisease(@PathVariable UUID id,
                                                                      @RequestBody UpdateDiseaseRequest request) {
        DiseaseResponse response = diseaseService.updateDisease(id, request);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Disease updated successfully", response));
    }

    // GET disease by id
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> getDiseaseById(@PathVariable UUID id) {
        Disease disease = diseaseService.getById(id);
        DiseaseResponse response = diseaseMapper.toResponse(disease);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Disease fetched successfully", response));
    }

    // DELETE disease
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteDisease(@PathVariable UUID id) {
        diseaseService.deleteDisease(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Disease deleted successfully", null));
    }
}