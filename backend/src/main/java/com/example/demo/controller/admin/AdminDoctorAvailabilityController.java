package com.example.demo.controller.admin;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/doctors/availability")
@RequiredArgsConstructor
public class AdminDoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;
    private final DoctorAvailabilityMapper mapper;

    @GetMapping("/get/{availabilityId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> getAvailabilityById(
            @PathVariable UUID availabilityId
    ) {
        var availability = availabilityService.getById(availabilityId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Availability retrieved successfully",
                        mapper.toResponse(availability)
                )
        );
    }

    @PostMapping("/create/{doctorId}")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> createForDoctor(
            @PathVariable UUID doctorId,
            @Valid @RequestBody AddAvailabilityRequest request) {

        DoctorAvailability entity = mapper.toEntity(request);
        DoctorAvailability saved = availabilityService.createForDoctor(doctorId, entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "Availability created successfully",
                mapper.toResponse(saved)
        ));
    }

    @PutMapping("/update/{doctorId}/{availabilityId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> updateForDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID availabilityId,
            @Valid @RequestBody UpdateAvailabilityRequest request) {

        DoctorAvailability updateEntity = mapper.toUpdateEntity(availabilityId, request);
        DoctorAvailability updated = availabilityService.updateForDoctor(doctorId, updateEntity);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability updated successfully",
                mapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/delete/{doctorId}/{availabilityId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteForDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID availabilityId) {

        availabilityService.deleteForDoctor(doctorId, availabilityId);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability deleted successfully",
                null
        ));
    }

    @GetMapping("/search")
    public List<DoctorAvailability> search(
            @RequestParam(required = false) UUID doctorId,
            DoctorAvailabilitySearchRequest request
    ) {
        return availabilityService.searchForAdmin(
                doctorId,
                request.getActive(),
                request.getFrom(),
                request.getTo()
        );
    }
}