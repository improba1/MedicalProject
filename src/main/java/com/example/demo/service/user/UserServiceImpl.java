package com.example.demo.service.user;

import com.example.demo.dto.request.user.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.logout.LogoutService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LogoutService logoutService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    // ==========================
    // 🔹 Бізнес-логіка для профілю
    // ==========================

    @Override
    public UserResponse getCurrentUserProfile() {
        User currentUser = getAuthenticatedUser();
        return userMapper.toResponse(currentUser);
    }

    @Override
    public UserResponse updateProfile(UpdateUserRequest request) {
        User currentUser = getAuthenticatedUser();
        userMapper.updateEntity(currentUser, request);
        User updated = userRepository.save(currentUser);
        return userMapper.toResponse(updated);
    }

    @Override
    public void deactivateProfile(HttpServletRequest request, HttpServletResponse response) {
        User currentUser = getAuthenticatedUser();
        currentUser.setActive(false);
        userRepository.save(currentUser);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    public void deactivateProfileById(UUID id, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setActive(false);
        userRepository.save(user);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }



    // ==========================
    // 🔹 Хелпер для отримання поточного користувача
    // ==========================
    private User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // у JWT username = email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));
    }
}