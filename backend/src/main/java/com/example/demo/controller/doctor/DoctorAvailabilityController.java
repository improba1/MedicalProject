package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor_availability.DoctorAvailabilityCreateRequest;
import com.example.demo.dto.request.doctor_availability.DoctorAvailabilitySearchRequest;
import com.example.demo.dto.request.doctor_availability.DoctorAvailabilityUpdateRequest;
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

    @GetMapping("/get/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> getMyAvailabilityById(
            @PathVariable UUID availabilityId
    ) {
        var availability = availabilityService.getByIdForAuthenticatedDoctor(availabilityId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Availability retrieved successfully",
                        mapper.toResponse(availability)
                )
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('doctor:create')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> create(
            @Valid @RequestBody DoctorAvailabilityCreateRequest request) {

        DoctorAvailability entity = mapper.toEntity(request);
        DoctorAvailability saved = availabilityService.create(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "Availability created successfully",
                mapper.toResponse(saved)
        ));
    }

    @PutMapping("/update/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> update(
            @PathVariable UUID availabilityId,
            @Valid @RequestBody DoctorAvailabilityUpdateRequest request) {

        DoctorAvailability updateEntity = mapper.toUpdateEntity(availabilityId, request);
        DoctorAvailability updated = availabilityService.updateExisting(updateEntity);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability updated successfully",
                mapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/delete/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID availabilityId) {
        availabilityService.deleteOwn(availabilityId);
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability deleted successfully",
                null
        ));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> search(
            @Valid DoctorAvailabilitySearchRequest request
    ) {
        var availabilities = availabilityService.searchForAuthenticatedDoctor(
                request.getActive(),
                request.getFrom(),
                request.getTo()
        );
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Availabilities retrieved successfully",
                        availabilities.stream()
                                .map(mapper::toResponse)
                                .toList()
                )
        );
    }

}