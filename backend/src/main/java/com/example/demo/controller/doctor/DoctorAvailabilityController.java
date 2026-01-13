package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
import com.example.demo.dto.request.doctor_availability.DoctorAvailabilitySearchRequest;
import com.example.demo.dto.request.doctor_availability.UpdateAvailabilityRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorAvailabilityResponse;
import com.example.demo.mapper.DoctorAvailabilityMapper;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.service.doctor.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctors/me/availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;
    private final DoctorAvailabilityMapper mapper;


    // ------------------ ADD ------------------
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('doctor:create')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> add(
            @Valid @RequestBody AddAvailabilityRequest request) {

        DoctorAvailability entity = mapper.toEntity(request);
        DoctorAvailability saved = availabilityService.create(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "Availability created successfully",
                mapper.toResponse(saved)
        ));
    }

    // ------------------ UPDATE ------------------
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAvailabilityRequest request) {

        DoctorAvailability updateEntity = mapper.toUpdateEntity(id, request);
        DoctorAvailability updated = availabilityService.updateExisting(updateEntity);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability updated successfully",
                mapper.toResponse(updated)
        ));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        availabilityService.deleteOwn(id);
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability deleted successfully",
                null
        ));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> search(
            @Valid DoctorAvailabilitySearchRequest request) {

        List<DoctorAvailabilityResponse> response = availabilityService
                .searchForAuthenticatedDoctor(request)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availabilities retrieved successfully",
                response
        ));
    }

}