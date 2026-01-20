package com.example.demo.controller.admin;

import com.example.demo.dto.request.user.UserUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.logout.LogoutService;
import com.example.demo.service.user.UserService;
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
    private final LogoutService logoutService;

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        User admin = userService.getCurrentUser();

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Admin profile fetched successfully",
                userMapper.toResponse(admin)
        ));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @RequestBody UserUpdateRequest request
    ) {
        User updatedAdmin = userService.updateCurrentUser(request);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Admin profile updated successfully",
                userMapper.toResponse(updatedAdmin)
        ));
    }

    @DeleteMapping("/deactivate")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deactivateProfile() {
        userService.deactivateCurrentUser();   // ✅ тільки бізнес-логіка
        logoutService.logoutCurrentUser();     // ✅ logout тут

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Admin profile deactivated and logged out successfully",
                null
        ));
    }
}