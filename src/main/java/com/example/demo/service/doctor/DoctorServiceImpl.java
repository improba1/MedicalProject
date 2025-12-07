package com.example.demo.service.doctor;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.mapper.DoctorMapper;
import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.model.Visit;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.specification.doctor.DoctorSpecificationBuilder;
import com.example.demo.service.logout.LogoutService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final VisitRepository visitRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper doctorMapper;
    private final LogoutService logoutService;

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
        Doctor doctor = getById(id);
        doctorRepository.delete(doctor);
    }

    @Override
    public List<Doctor> searchDoctors(DoctorSearchRequest request) {
        return doctorRepository.findAll(DoctorSpecificationBuilder.build(request));
    }
    /////////////////////////////////////////////////
    @Override
    public List<Visit> getVisits(UUID doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<DoctorAvailability> getAvailability(UUID doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    public Visit cancelVisit(UUID visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        visit.setStatus(VisitStatus.CANCELED);
        return visitRepository.save(visit);
    }

    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        visit.setAppointmentTime(newTime);
        visit.setStatus(VisitStatus.RESCHEDULED);
        return visitRepository.save(visit);
    }
/// ///////////////////
    @Override
    public DoctorResponse getCurrentDoctorProfile() {
        Doctor currentDoctor = getAuthenticatedDoctor();
        return doctorMapper.toResponse(currentDoctor);
    }

    @Override
    public DoctorResponse updateDoctorProfile(UpdateDoctorRequest request) {
        Doctor currentDoctor = getAuthenticatedDoctor();
        doctorMapper.updateEntity(currentDoctor, request);
        return doctorMapper.toResponse(doctorRepository.save(currentDoctor));
    }

    @Override
    public void deleteDoctorProfile(HttpServletRequest request, HttpServletResponse response) {
        Doctor currentDoctor = getAuthenticatedDoctor();
        currentDoctor.setActive(false);
        doctorRepository.save(currentDoctor);

        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    public Doctor getAuthenticatedDoctor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }

}