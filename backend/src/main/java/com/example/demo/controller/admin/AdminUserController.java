package com.example.demo.controller.admin;

import com.example.demo.dto.request.user.UserSearchRequest;
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

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        User user = userService.getById(userId);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User fetched successfully", response));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> search(@RequestBody UserSearchRequest request) {
        return ResponseEntity.ok(
                userService.search(request)
        );
    }

    @DeleteMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deactivateProfileById(@PathVariable UUID id,
                                                                   HttpServletRequest request,
                                                                   HttpServletResponse response) {
        userService.deactivateUserById(id, request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Profile with ID " + id + " deactivated and logged out successfully", null));
    }
}