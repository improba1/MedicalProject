package com.example.demo.controller.patient;

import com.example.demo.dto.request.doctor_availability.DoctorAvailabilitySearchRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorAvailabilityResponse;
import com.example.demo.mapper.DoctorAvailabilityMapper;
import com.example.demo.service.doctor.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/patient/availabilities")
@RequiredArgsConstructor
public class PatientDoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;
    private final DoctorAvailabilityMapper mapper;

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> search(
            @Valid DoctorAvailabilitySearchRequest request
    ) {

        var availabilities = availabilityService.searchForAuthenticatedPatient(
                request.getDoctorId(),
                request.getFrom(),
                request.getTo()
        );

        var response = availabilities.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Availabilities retrieved successfully",
                        response
                )
        );
    }
}