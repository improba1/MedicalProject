package com.example.demo.service.logout;

public interface LogoutService {
    void logoutCurrentUser();

    void forceLogoutUser(String username);
}