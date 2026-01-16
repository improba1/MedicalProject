package com.example.demo.controller.public_;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.service.doctor.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/doctors/public")
@RequiredArgsConstructor
public class DoctorPublicController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> searchPublic(
            @RequestBody DoctorSearchRequest request
    ) {
        var doctors = doctorService.searchPublicDoctors(
                request.getName(),
                request.getSpecialization(),
                request.getRating()
        );

        var response = doctorMapper.toResponseList(doctors);

        return ResponseEntity.ok(
                ApiResponse.of(200, "Doctors fetched successfully", response)
        );
    }
}