package com.example.demo.repository;

import com.example.demo.model.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, UUID>, JpaSpecificationExecutor<MedicalService> {
}