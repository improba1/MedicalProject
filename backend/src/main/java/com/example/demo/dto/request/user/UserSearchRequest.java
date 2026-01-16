package com.example.demo.dto.request.user;

import com.example.demo.enums.Role;
import com.example.demo.enums.Sex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private String email;
    private String nickname;
    private String phone;
    private String name;
    private Role role;
    private Sex sex;
    private Boolean isActive;
}