package com.example.demo.backend_patient.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter 
@Getter
@AllArgsConstructor
@Table(name = "patients")
public class Patient {

    public Patient(){

    }
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String last_name;
    private short age;
    private String login;
    private String password;
    private boolean active = true;
    private String role;
}
