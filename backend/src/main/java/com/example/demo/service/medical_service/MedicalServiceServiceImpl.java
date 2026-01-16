package com.example.demo.service.medical_service;

import com.example.demo.model.Doctor;
import com.example.demo.model.MedicalService;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.MedicalServiceRepository;
import com.example.demo.repository.specification.medical_service.MedicalServiceSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalServiceServiceImpl implements MedicalServiceService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final CurrentUserService currentUserService;
    private final DoctorRepository doctorRepository;

    @Override
    public MedicalService getById(UUID id) {
        return medicalServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical service not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalService getByIdForAuthenticatedDoctor(UUID id) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();

        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical service not found"));

        if (!service.getDoctor().getId().equals(doctorId)) {
            throw new AccessDeniedException("You can access only your own medical services");
        }
        return service;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalService> searchForAdmin(UUID doctorId, String name, Boolean active, BigDecimal minPrice, BigDecimal maxPrice) {
        return medicalServiceRepository.findAll(
                MedicalServiceSpecificationBuilder.buildForAdmin(
                        doctorId, name, active, minPrice, maxPrice
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalService> searchForAuthenticatedDoctor(String name, Boolean active, BigDecimal minPrice, BigDecimal maxPrice) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();

        return medicalServiceRepository.findAll(
                MedicalServiceSpecificationBuilder.buildForDoctor(
                        doctorId, name, active, minPrice, maxPrice
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalService> searchForPatient(UUID doctorId, String name, BigDecimal minPrice, BigDecimal maxPrice) {
        return medicalServiceRepository.findAll(
                MedicalServiceSpecificationBuilder.buildForPatient(
                        doctorId, name, minPrice, maxPrice
                )
        );
    }

    @Transactional
    @Override
    public MedicalService createForDoctor(UUID doctorId, MedicalService service) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        service.setId(null);
        service.setDoctor(doctor);
        service.setActive(true);

        return medicalServiceRepository.save(service);
    }

    @Transactional
    @Override
    public MedicalService updateForDoctor(UUID doctorId, MedicalService service) {
        Doctor doctor = doctorRepository.findById(service.getDoctor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        service.setDoctor(doctor);
        return medicalServiceRepository.save(service);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        MedicalService service = getById(id);
        medicalServiceRepository.delete(service);
    }

    @Transactional
    @Override
    public MedicalService createForAuthenticatedDoctor(MedicalService service) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        service.setId(null);
        service.setDoctor(doctor);
        service.setActive(true);
        return medicalServiceRepository.save(service);
    }

    @Transactional
    @Override
    public MedicalService updateForAuthenticatedDoctor(MedicalService entity) {
        MedicalService existing = getById(entity.getId());
        Doctor doctor = currentUserService.getAuthenticatedDoctor();

        if (!existing.getDoctor().getId().equals(doctor.getId())) {
            throw new AccessDeniedException("You can update only your own services");
        }
        existing.setName(entity.getName());
        existing.setDescription(entity.getDescription());
        existing.setPrice(entity.getPrice());
        existing.setActive(entity.isActive());

        return medicalServiceRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteForAuthenticatedDoctor(UUID id) {
        MedicalService service = getById(id);
        Doctor doctor = currentUserService.getAuthenticatedDoctor();

        if (!service.getDoctor().getId().equals(doctor.getId())) {
            throw new AccessDeniedException("You can delete only your own medical services");
        }

        medicalServiceRepository.delete(service);
    }
}