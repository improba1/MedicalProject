package com.example.demo.service.user;

import com.example.demo.dto.request.user.UserUpdateRequest;
import com.example.demo.dto.request.user.UserSearchRequest;
import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getById(UUID id);
    List<User> search(UserSearchRequest request);

    User create(User user);
    User update(User user);
    void delete(UUID id);

    User getCurrentUser();
    User updateCurrentUser(UserUpdateRequest request);
    User deactivateCurrentUser();
    User deactivateUserById(UUID id);
}
