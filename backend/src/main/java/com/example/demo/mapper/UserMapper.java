package com.example.demo.mapper;

import com.example.demo.dto.request.user.AddUserRequest;
import com.example.demo.dto.request.user.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.enums.Role;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(AddUserRequest request) {
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .phone(request.getPhone())
                .address(request.getAddress())
                .birthDate(request.getBirthDate())   // 🔹 додаємо дату народження
                .sex(request.getSex())               // 🔹 додаємо стать
                .role(Role.ADMIN)                    // 🔹 автоматично призначаємо роль ADMIN
                .isActive(true)
                .build();
    }

    public void updateEntity(User user, UpdateUserRequest request) {
        if (request.getFirstname() != null) user.setFirstname(request.getFirstname());
        if (request.getLastname() != null) user.setLastname(request.getLastname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getBirthDate() != null) user.setBirthDate(request.getBirthDate()); // 🔹 оновлення дати народження
        if (request.getSex() != null) user.setSex(request.getSex());                   // 🔹 оновлення статі
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .isActive(user.isActive())
                .sex(user.getSex() != null ? user.getSex().name() : null)
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}