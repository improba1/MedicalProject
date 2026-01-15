package com.example.demo.service.user;

import com.example.demo.dto.request.user.UpdateUserRequest;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.auth.CurrentUserService;
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
    private final CurrentUserService currentUserService;

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
    public Optional<User> getByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        return userRepository.findByPhone(phone);
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
    // 🔹 Профіль залогованого користувача
    // ==========================

    @Override
    public User getCurrentUser() {
        return currentUserService.getAuthenticatedUser();
    }

    @Override
    public User updateCurrentUser(UpdateUserRequest request) {
        User currentUser = currentUserService.getAuthenticatedUser();
        userMapper.updateEntity(currentUser, request);
        return userRepository.save(currentUser);
    }

    @Override
    public void deactivateCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        User currentUser = currentUserService.getAuthenticatedUser();
        currentUser.setActive(false);
        userRepository.save(currentUser);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    public void deactivateUserById(UUID id, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setActive(false);
        userRepository.save(user);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }
}