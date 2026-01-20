package com.example.demo.controller.admin;

import com.example.demo.dto.request.user.UserSearchRequest;
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

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final LogoutService logoutService;

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable UUID userId
    ) {
        User user = userService.getById(userId);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "User fetched successfully",
                userMapper.toResponse(user)
        ));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<?>> search(
            @RequestBody UserSearchRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                userService.search(request)
        ));
    }

    @DeleteMapping("/deactivate/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deactivateUserById(
            @PathVariable UUID userId
    ) {
        User user = userService.deactivateUserById(userId);

        logoutService.forceLogoutUser(user.getEmail());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "User with ID " + userId + " deactivated and logged out successfully",
                null
        ));
    }
}