package com.example.demo.controller.admin;

import com.example.demo.dto.request.user.UserUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/admin/me/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/get")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        User admin = userService.getCurrentUser();
        UserResponse response = userMapper.toResponse(admin);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Admin profile fetched successfully", response));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@RequestBody UserUpdateRequest request) {
        User updatedAdmin = userService.updateCurrentUser(request);
        UserResponse response = userMapper.toResponse(updatedAdmin);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Admin profile updated successfully", response));
    }

    @DeleteMapping("/deactivate")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deactivateProfile(HttpServletRequest request, HttpServletResponse response) {
        userService.deactivateCurrentUser(request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "Admin profile deactivated and logged out successfully", null));
    }
}