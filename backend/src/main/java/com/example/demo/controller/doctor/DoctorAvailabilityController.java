package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
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

    // ------------------ GET ALL ------------------
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getMyAvailability() {
        List<DoctorAvailability> list = availabilityService.getOwnAvailabilities();
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Doctor availability fetched successfully",
                mapper.toResponseList(list)
        ));
    }

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
    @PutMapping("/update/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> update(
            @PathVariable UUID availabilityId,
            @Valid @RequestBody UpdateAvailabilityRequest request) {

        DoctorAvailability entity = mapper.toEntity(request);
        entity.setId(availabilityId);

        DoctorAvailability updated = availabilityService.updateExisting(entity);

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

    // ------------------ FILTERS ------------------
    @GetMapping("/get/today")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> today() {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnToday())
        ));
    }

    @GetMapping("/get/next-hour")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> nextHour() {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnNextHour())
        ));
    }

    @GetMapping("/get/this-week")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> thisWeek() {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnThisWeek())
        ));
    }

    @GetMapping("/get/next-week")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> nextWeek() {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnNextWeek())
        ));
    }

    @GetMapping("/get/month/{year}/{month}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> byMonth(
            @PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnByMonth(year, month))
        ));
    }

    @GetMapping("/get/day/{year}/{month}/{day}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> byDay(
            @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnByDay(year, month, day))
        ));
    }

    @GetMapping("/get/year/{year}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> byYear(@PathVariable int year) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Fetched",
                mapper.toResponseList(availabilityService.getOwnByYear(year))
        ));
    }
}