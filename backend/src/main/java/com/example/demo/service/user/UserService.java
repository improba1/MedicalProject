package com.example.demo.service.user;

import com.example.demo.dto.request.user.UpdateUserRequest;
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
    Optional<User> getByNickname(String nickname);
    Optional<User> getByPhone(String phone);

    User create(User user);
    User update(User user);
    void delete(UUID id);

    // ==========================
    // 🔹 Профіль
    // ==========================
    User getCurrentUser();
    User updateCurrentUser(UpdateUserRequest request);
    void deactivateCurrentUser(HttpServletRequest request, HttpServletResponse response);
    void deactivateUserById(UUID id, HttpServletRequest request, HttpServletResponse response);
}
