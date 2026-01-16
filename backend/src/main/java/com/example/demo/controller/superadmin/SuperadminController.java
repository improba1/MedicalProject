package com.example.demo.controller.superadmin;

import com.example.demo.dto.request.user.UserCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/superadmin/users")
@RequiredArgsConstructor
public class SuperadminController {

    private final UserService userService;
    private final UserMapper  userMapper;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('superadmin:create')")
    public ResponseEntity<ApiResponse<UserResponse>> createAdmin(@Valid @RequestBody UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        User saved = userService.create(user);
        UserResponse response = userMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED.value(), "Admin created successfully", response));
    }

    @DeleteMapping("/hard-delete/{userId}")
    @PreAuthorize("hasAuthority('superadmin:delete')")
    public ResponseEntity<ApiResponse<Void>> hardDeleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), "User permanently deleted", null));
    }
}