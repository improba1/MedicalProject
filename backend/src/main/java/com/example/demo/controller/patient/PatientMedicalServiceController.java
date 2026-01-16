package com.example.demo.controller.patient;

import com.example.demo.dto.request.medical_service.MedicalServiceSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.MedicalServiceResponse;
import com.example.demo.mapper.MedicalServiceMapper;
import com.example.demo.service.medical_service.MedicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/patient/medical-services")
@RequiredArgsConstructor
public class PatientMedicalServiceController {

    private final MedicalServiceService medicalServiceService;
    private final MedicalServiceMapper medicalServiceMapper;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicalServiceResponse>>> search(
            @RequestBody MedicalServiceSearchRequest req
    ) {
        var services = medicalServiceService.searchForPatient(
                req.getDoctorId(),
                req.getName(),
                req.getMinPrice(),
                req.getMaxPrice()
        );
        return ResponseEntity.ok(
                ApiResponse.of(200, "Services fetched successfully", medicalServiceMapper.toResponseList(services))
        );
    }
}