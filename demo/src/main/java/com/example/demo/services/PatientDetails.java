package com.example.demo.services;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.backend_patient.model.Patient;
import com.example.demo.backend_patient.repos.PatientRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientDetails implements UserDetailsService {
    private final PatientRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Patient user = userRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("User" + username + " doesn't exist"));
        if (!user.isActive()) {
            throw new UsernameNotFoundException("User is deactivated");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            user.isActive(),
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            List.of(authority)
        );
    }
}