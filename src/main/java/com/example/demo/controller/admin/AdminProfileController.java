package com.example.demo.controller.admin;

import com.example.demo.dto.request.user.UpdateUserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserService userService;

    // 🔹 Подивитись свій профіль
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/get")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        UserResponse response = userService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Admin profile fetched successfully", response));
    }

    // 🔹 Оновити профіль
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Admin profile updated successfully", response));
    }

    // 🔹 Видалити профіль (з автоматичним logout)
    @DeleteMapping("/deactivate")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deactivateProfile(HttpServletRequest request, HttpServletResponse response) {
        userService.deactivateProfile(request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Admin profile deleted and logged out successfully", null));
    }
}