package com.example.demo.service.medical_service;

import com.example.demo.dto.request.medical_service.MedicalServiceSearchRequest;
import com.example.demo.model.Doctor;
import com.example.demo.model.MedicalService;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.MedicalServiceRepository;
import com.example.demo.repository.specification.medical_service.MedicalServiceSpecification;
import com.example.demo.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // ==========================
    // 🔹 Пошук для адміна та публічного користувача
    // ==========================
    public List<MedicalService> search(UUID doctorId, MedicalServiceSearchRequest request) {
        return medicalServiceRepository.findAll(
                MedicalServiceSpecification.search(
                        doctorId,
                        request.getActive(),
                        request.getName(),
                        request.getMinPrice(),
                        request.getMaxPrice()
                )
        );
    }


    // ==========================
    // 🔹 Пошук для залогованого лікаря (тільки свої сервіси)
    // ==========================
    @Override
    public List<MedicalService> searchForAuthenticatedDoctor(MedicalServiceSearchRequest request) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
            return medicalServiceRepository.findAll(
                    MedicalServiceSpecification.search(
                            doctor.getId(),
                            request.getActive(),
                            request.getName(),
                            request.getMinPrice(),
                            request.getMaxPrice()
                    )
            );
    }


    // ==========================
    // 🔹 Створення сервісу
    // ==========================
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
        Doctor doctor = doctorRepository.findById(doctorId)
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

    // ==========================
    // 🔹 CRUD для залогованого лікаря
    // ==========================
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

        // 🔹 Обновлюємо поля
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