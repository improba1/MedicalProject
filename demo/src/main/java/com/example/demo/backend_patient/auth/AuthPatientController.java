package com.example.demo.backend_patient.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.backend_patient.model.Patient;
import com.example.demo.backend_patient.repos.PatientRepository;
import com.example.demo.dto.PatientLoginRequest;
import com.example.demo.dto.PatientLoginResponse;
import com.example.demo.dto.PatientRegisterRequest;
import com.example.demo.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient/auth")
public class AuthPatientController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody PatientLoginRequest loginRequest) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getLogin(),
                loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrang password or login!");
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        PatientLoginResponse responseBody = new PatientLoginResponse(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody.getToken().toString());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PatientRegisterRequest regRequest) {
        if (patientRepository.findByLogin(regRequest.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists.");
        }
        Patient patient = new Patient();
        patient.setName(regRequest.getName());
        patient.setLast_name(regRequest.getLast_name());
        patient.setAge(regRequest.getAge());
        patient.setLogin(regRequest.getLogin());
        patient.setPassword(passwordEncoder.encode(regRequest.getPassword())); 
        patient.setActive(true);
        patient.setRole("USER");
        patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
    
}
