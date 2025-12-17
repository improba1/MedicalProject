package com.example.demo.service.doctor;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.model.Doctor;
import com.example.demo.model.Image;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.specification.doctor.DoctorSpecificationBuilder;
import com.example.demo.service.image.ImageService;
import com.example.demo.service.logout.LogoutService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogoutService logoutService;
    private final ImageService imageService;

    @Override
    public Doctor getById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor create(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor update(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(UUID id) {
        doctorRepository.delete(getById(id));
    }

    @Override
    public List<Doctor> searchDoctors(DoctorSearchRequest request) {
        return doctorRepository.findAll(DoctorSpecificationBuilder.build(request));
    }

    @Override
    public Doctor getCurrentDoctor() {
        return getAuthenticatedDoctor();
    }

    @Override
    public Doctor updateCurrentDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateOwnProfileWithImage(Doctor doctor, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            Image image = (doctor.getImage() == null)
                    ? imageService.saveImage(doctor.getId(), file)
                    : imageService.updateImage(doctor.getImage().getId(), file);
            doctor.setImage(image);
        }
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctorWithImage(UUID doctorId, UpdateDoctorRequest request, MultipartFile imageFile) {
        Doctor doctor = getById(doctorId);
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = (doctor.getImage() == null)
                    ? imageService.saveImage(doctor.getId(), imageFile)
                    : imageService.updateImage(doctor.getImage().getId(), imageFile);

            doctor.setImage(image);
        }

        return doctorRepository.save(doctor);
    }

    @Override
    public void deactivateDoctorProfile(HttpServletRequest request, HttpServletResponse response) {
        Doctor currentDoctor = getAuthenticatedDoctor();
        currentDoctor.setActive(false);
        doctorRepository.save(currentDoctor);

        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    private Doctor getAuthenticatedDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }
}