package com.example.demo.controller.admin;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
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

    // ------------------ HELPERS ------------------

    private ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> okList(List<DoctorAvailability> list, String message) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                message,
                mapper.toResponseList(list)
        ));
    }

    private ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> okOne(DoctorAvailability entity) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Availability updated successfully",
                mapper.toResponse(entity)
        ));
    }

    // ------------------ GET ALL BY DOCTOR ------------------
    @GetMapping("/get/{doctorId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getByDoctor(@PathVariable UUID doctorId) {
        return okList(
                availabilityService.getByDoctor(doctorId),
                "Doctor availability fetched successfully"
        );
    }

    // ------------------ CREATE ------------------
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

    // ------------------ UPDATE ------------------
    @PutMapping("/update/{doctorId}/{availabilityId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> updateForDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID availabilityId,
            @Valid @RequestBody UpdateAvailabilityRequest request) {

        DoctorAvailability updateEntity = mapper.toUpdateEntity(availabilityId, request);
        DoctorAvailability updated = availabilityService.updateForDoctor(doctorId, updateEntity);

        return okOne(updated);
    }

    // ------------------ DELETE ------------------
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

    // ------------------ FILTERS ------------------
    // ------------------ GET ACTIVE SLOTS ------------------
    @GetMapping("/get/{doctorId}/active")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getActiveSlots(
            @PathVariable UUID doctorId) {

        return okList(
                availabilityService.getActiveSlots(doctorId),
                "Active doctor availability fetched successfully"
        );
    }


    @GetMapping("/get/{doctorId}/today")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> today(@PathVariable UUID doctorId) {
        return okList(
                availabilityService.getToday(doctorId),
                "Fetched"
        );
    }

    @GetMapping("/get/{doctorId}/next-hour")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> nextHour(@PathVariable UUID doctorId) {
        return okList(
                availabilityService.getNextHour(doctorId),
                "Fetched"
        );
    }

    @GetMapping("/get/{doctorId}/this-week")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> thisWeek(@PathVariable UUID doctorId) {
        return okList(
                availabilityService.getThisWeek(doctorId),
                "Fetched"
        );
    }

    @GetMapping("/get/{doctorId}/next-week")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> nextWeek(@PathVariable UUID doctorId) {
        return okList(
                availabilityService.getNextWeek(doctorId),
                "Fetched"
        );
    }

    @GetMapping("/get/{doctorId}/month/{year}/{month}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> byMonth(
            @PathVariable UUID doctorId,
            @PathVariable int year,
            @PathVariable int month) {

        return okList(
                availabilityService.getByMonth(doctorId, year, month),
                "Fetched"
        );
    }

    @GetMapping("/get/{doctorId}/day/{year}/{month}/{day}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> byDay(
            @PathVariable UUID doctorId,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day) {

        return okList(
                availabilityService.getByDay(doctorId, year, month, day),
                "Fetched"
        );
    }

    @GetMapping("/get/{doctorId}/year/{year}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> byYear(
            @PathVariable UUID doctorId,
            @PathVariable int year) {

        return okList(
                availabilityService.getByYear(doctorId, year),
                "Fetched"
        );
    }
}