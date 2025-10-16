package com.example.demo.dto;

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
}
