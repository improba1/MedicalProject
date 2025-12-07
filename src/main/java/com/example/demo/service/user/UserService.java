package com.example.demo.service.user;

import com.example.demo.dto.request.user.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User getById(UUID id);

    List<User> getAll();

    Optional<User> getByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(UUID id);

    // 🔹 нові методи для контролера
    UserResponse getCurrentUserProfile();

    UserResponse updateProfile(UpdateUserRequest request);

    void deactivateProfile(HttpServletRequest request, HttpServletResponse response);

    void deactivateProfileById(UUID id, HttpServletRequest request, HttpServletResponse response);
}