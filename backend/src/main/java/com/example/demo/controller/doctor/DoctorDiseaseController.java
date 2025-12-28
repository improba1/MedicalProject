package com.example.demo.controller.doctor;

import com.example.demo.dto.response.DiseaseResponse;
import com.example.demo.mapper.DiseaseMapper;
import com.example.demo.model.Disease;
import com.example.demo.service.disease.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/diseases")
@RequiredArgsConstructor
public class DoctorDiseaseController {

    private final DiseaseService diseaseService;
    private final DiseaseMapper diseaseMapper;

    // 🔹 Пошук за кодом
    @GetMapping("/search/code")
    @PreAuthorize("hasAnyAuthority('doctor:read', 'admin:read')")
    public ResponseEntity<List<DiseaseResponse>> searchByCode(@RequestParam String code) {
        List<Disease> diseases = diseaseService.searchByCode(code);
        return ResponseEntity.ok(diseases.stream().map(diseaseMapper::toResponse).toList());
    }

    // 🔹 Пошук за назвою
    @GetMapping("/search/name")
    @PreAuthorize("hasAnyAuthority('doctor:read', 'admin:read')")
    public ResponseEntity<List<DiseaseResponse>> searchByName(@RequestParam String name) {
        List<Disease> diseases = diseaseService.searchByName(name);
        return ResponseEntity.ok(diseases.stream().map(diseaseMapper::toResponse).toList());
    }

    // 🔹 Комбінований пошук
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('doctor:read', 'admin:read')")
    public ResponseEntity<List<DiseaseResponse>> search(@RequestParam String query) {
        List<Disease> diseases = diseaseService.search(query);
        return ResponseEntity.ok(diseases.stream().map(diseaseMapper::toResponse).toList());
    }
}