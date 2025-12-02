package com.example.demo.controller.doctor;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
import com.example.demo.dto.request.doctor_availability.UpdateAvailabilityRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.DoctorAvailabilityResponse;
import com.example.demo.mapper.doctor.DoctorAvailabilityMapper;
import com.example.demo.model.User;
import com.example.demo.service.doctors_availability_service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/doctors/me/availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;
    private final DoctorAvailabilityMapper availabilityMapper;

    // 🔹 Отримати всі свої слоти
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getMyAvailability(
            @AuthenticationPrincipal User user) {

        var availabilities = availabilityService.getByDoctor(user.getId());
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability fetched successfully", responses));
    }

    // 🔹 Додати слот
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('doctor:create')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> addMyAvailability(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddAvailabilityRequest request) {

        var saved = availabilityService.addAvailability(user.getId(), request);
        var response = availabilityMapper.toResponse(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(),
                        "Doctor availability created successfully", response));
    }

    // 🔹 Оновити слот
    @PutMapping("/update/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> updateMyAvailability(
            @AuthenticationPrincipal User user,
            @PathVariable UUID availabilityId,
            @Valid @RequestBody UpdateAvailabilityRequest request) {

        var updated = availabilityService.updateForDoctor(user.getId(), availabilityId, request);
        var response = availabilityMapper.toResponse(updated);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability updated successfully", response));
    }

    // 🔹 Видалити слот
    @DeleteMapping("/delete/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:delete')")
    public ResponseEntity<Void> deleteMyAvailability(
            @AuthenticationPrincipal User user,
            @PathVariable UUID availabilityId) {

        availabilityService.deleteForDoctor(user.getId(), availabilityId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 🔹 Отримати конкретний слот
    @GetMapping("/{availabilityId}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<DoctorAvailabilityResponse>> getAvailabilityById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID availabilityId) {

        var availability = availabilityService.getById(availabilityId);
        var response = availabilityMapper.toResponse(availability);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability fetched successfully", response));
    }

    // 🔹 Пошук за датами
    @GetMapping("/today")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getToday(
            @AuthenticationPrincipal User user) {

        var availabilities = availabilityService.getToday(user.getId());
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for today fetched successfully", responses));
    }

    @GetMapping("/next-hour")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getNextHour(
            @AuthenticationPrincipal User user) {

        var availabilities = availabilityService.getNextHour(user.getId());
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for next hour fetched successfully", responses));
    }

    @GetMapping("/this-week")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getThisWeek(
            @AuthenticationPrincipal User user) {

        var availabilities = availabilityService.getThisWeek(user.getId());
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for this week fetched successfully", responses));
    }

    @GetMapping("/next-week")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getNextWeek(
            @AuthenticationPrincipal User user) {

        var availabilities = availabilityService.getNextWeek(user.getId());
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for next week fetched successfully", responses));
    }

    @GetMapping("/month/{year}/{month}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getByMonth(
            @AuthenticationPrincipal User user,
            @PathVariable int year,
            @PathVariable int month) {

        var availabilities = availabilityService.getByMonth(user.getId(), year, month);
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for month fetched successfully", responses));
    }

    @GetMapping("/day/{year}/{month}/{day}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getByDay(
            @AuthenticationPrincipal User user,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day) {

        var availabilities = availabilityService.getByDay(user.getId(), year, month, day);
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for day fetched successfully", responses));
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityResponse>>> getByYear(
            @AuthenticationPrincipal User user,
            @PathVariable int year) {

        var availabilities = availabilityService.getByYear(user.getId(), year);
        var responses = availabilityMapper.toResponseList(availabilities);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Doctor availability for year fetched successfully", responses));
    }
}