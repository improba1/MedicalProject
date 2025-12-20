package com.example.demo.controller.public_;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/doctors")
@RequiredArgsConstructor
public class PublicDoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final ImageService imageService;

    // Отримати всіх лікарів
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAll();
        List<DoctorResponse> responses = doctorMapper.toResponseList(doctors);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Doctors fetched successfully", responses));
    }

    // 🔹 Новий ендпоінт: пошук лікарів
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> searchDoctors(@RequestBody DoctorSearchRequest request) {
        List<Doctor> doctors = doctorService.searchDoctors(request);
        List<DoctorResponse> responses = doctorMapper.toResponseList(doctors);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Doctors search results", responses));
    }
}