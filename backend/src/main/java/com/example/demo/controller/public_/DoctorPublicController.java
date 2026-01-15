package com.example.demo.controller.public_;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.request.medical_service.MedicalServiceSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.MedicalServiceResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.mapper.MedicalServiceMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import com.example.demo.service.medical_service.MedicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctors")
@RequiredArgsConstructor
public class DoctorPublicController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final MedicalServiceService medicalServiceService;
    private final MedicalServiceMapper medicalServiceMapper;

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAll();
        List<DoctorResponse> responses = doctorMapper.toResponseList(doctors);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Doctors fetched successfully", responses));
    }

    @PostMapping("/search-doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> searchDoctors(@RequestBody DoctorSearchRequest request) {
        List<Doctor> doctors = doctorService.searchDoctors(request);
        List<DoctorResponse> responses = doctorMapper.toResponseList(doctors);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Doctors search results", responses));
    }

    @PostMapping("/{doctorId}/search-service")
    public List<MedicalServiceResponse> search(@PathVariable UUID doctorId, @RequestBody MedicalServiceSearchRequest request) {
        request.setActive(true);

        return medicalServiceService.search(doctorId, request).stream()
                .map(medicalServiceMapper::toResponse)
                .toList();
    }
}