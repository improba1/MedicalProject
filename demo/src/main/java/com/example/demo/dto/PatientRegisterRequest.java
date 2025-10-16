package com.example.demo.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientRegisterRequest {
    private String name;
    private String last_name;
    private short age;
    private String login;
    private String password;
    private boolean active = true;
    private String email;
    private String phone;
}
