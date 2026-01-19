package com.example.demo.service.doctor;

import com.example.demo.enums.Specialization;
import com.example.demo.model.Doctor;
import com.example.demo.model.Image;
import com.example.demo.model.Patient;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.specification.doctor.DoctorSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.image.ImageService;
import com.example.demo.service.logout.LogoutService;
import com.example.demo.service.patient.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CurrentUserService currentUserService;
    private final VisitRepository visitRepository;
    private final PatientService patientService;

    private void handleImage(Doctor doctor, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) return;

        Image image = (doctor.getImage() == null)
                ? imageService.saveImage(doctor.getId(), imageFile)
                : imageService.updateImage(doctor.getImage().getId(), imageFile);

        doctor.setImage(image);
    }

    private void mergeDoctorForAdmin(Doctor target, Doctor source) {
        if (source.getQualification() != null)
            target.setQualification(source.getQualification());

        if (source.getRating() != null)
            target.setRating(source.getRating());

        if (source.getAddress() != null)
            target.setAddress(source.getAddress());
    }

    private void mergeDoctorForSelf(Doctor target, Doctor source) {
        if (source.getQualification() != null)
            target.setQualification(source.getQualification());

        if (source.getAddress() != null)
            target.setAddress(source.getAddress());
    }

    @Override
    public Doctor getById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> searchDoctors(String name, Specialization specialization, Double rating, Boolean active) {
        return doctorRepository.findAll(
                DoctorSpecificationBuilder.build(name, specialization, rating, active)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> searchPublicDoctors(String name, Specialization specialization, Double rating) {
        return doctorRepository.findAll(
                DoctorSpecificationBuilder.buildForPublic(name, specialization, rating)
        );
    }

    @Override
    public Doctor create(Doctor doctor, MultipartFile image) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setActive(true);

        Doctor saved = doctorRepository.save(doctor);

        handleImage(saved, image);

        return doctorRepository.save(saved);
    }

    @Override
    public Doctor updateDoctorByAdmin(UUID doctorId, Doctor updated, MultipartFile image) {
        Doctor existing = getById(doctorId);

        mergeDoctorForAdmin(existing, updated);
        handleImage(existing, image);

        return doctorRepository.save(existing);
    }

    @Override
    public Doctor activateDoctor(UUID id) {
        Doctor doctor = getById(id);
        doctor.setActive(true);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor deactivateDoctor(UUID id) {
        Doctor doctor = getById(id);
        doctor.setActive(false);
        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(UUID id) {
        doctorRepository.delete(getById(id));
    }

    @Override
    public Doctor getCurrentDoctor() {
        return currentUserService.getAuthenticatedDoctor();
    }

    @Override
    public Doctor updateCurrentDoctor(Doctor updated, MultipartFile image) {
        Doctor current = currentUserService.getAuthenticatedDoctor();

        mergeDoctorForSelf(current, updated);
        handleImage(current, image);

        return doctorRepository.save(current);
    }

    @Override
    public Doctor updateCurrentDoctorImage(MultipartFile image) {
        Doctor current = currentUserService.getAuthenticatedDoctor();
        handleImage(current, image);
        return doctorRepository.save(current);
    }


    @Override
    public void deactivateCurrentDoctor(HttpServletRequest request, HttpServletResponse response) {
        Doctor current = currentUserService.getAuthenticatedDoctor();
        current.setActive(false);
        doctorRepository.save(current);

        logoutService.logout(
                request,
                response,
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Override
    public Patient getPatientIfDoctorHasAccess(UUID patientId) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();

        boolean allowed = visitRepository.existsByDoctorIdAndPatientId(
                doctor.getId(),
                patientId
        );

        if (!allowed) {
            throw new SecurityException("You do not have access to this patient");
        }

        return patientService.getById(patientId);
    }
}