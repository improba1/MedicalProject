package com.example.demo.service.user;

import com.example.demo.dto.request.user.UserUpdateRequest;
import com.example.demo.dto.request.user.UserSearchRequest;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.specification.user.UserSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.logout.LogoutServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LogoutServiceImpl logoutService;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> search(UserSearchRequest request) {
        return userRepository.findAll(
                UserSpecificationBuilder.build(request)
        );
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
    public User getCurrentUser() {
        return currentUserService.getAuthenticatedUser();
    }

    @Override
    public User updateCurrentUser(UserUpdateRequest request) {
        User currentUser = currentUserService.getAuthenticatedUser();
        userMapper.updateEntity(currentUser, request);
        return userRepository.save(currentUser);
    }

    @Override
    public void delete(UUID id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    @Override
    public User deactivateCurrentUser() {
        User currentUser = currentUserService.getAuthenticatedUser();
        currentUser.setActive(false);
        return userRepository.save(currentUser);
    }

    @Override
    public User deactivateUserById(UUID id) {
        User user = getById(id);
        user.setActive(false);
        return userRepository.save(user);
    }
}