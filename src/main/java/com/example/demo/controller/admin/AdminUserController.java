package com.example.demo.controller.admin;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID id) {
        User user = userService.getById(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User fetched successfully", response));
    }

    // 🔹 Отримати всіх користувачів
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserResponse> responses = userMapper.toResponseList(users);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "All users fetched successfully", responses));
    }

    // 🔹 Деактивувати профіль за ID (з автоматичним logout)
    @DeleteMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deactivateProfileById(@PathVariable UUID id,
                                                                   HttpServletRequest request,
                                                                   HttpServletResponse response) {
        userService.deactivateProfileById(id, request, response);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),
                "Profile with ID " + id + " deactivated and logged out successfully", null));
    }
}