package com.example.demo.repository;

import com.example.demo.model.VisitServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitServiceItemRepository extends JpaRepository<VisitServiceItem, UUID> {

    List<VisitServiceItem> findByVisitId(UUID visitId);

    void deleteByVisitId(UUID visitId);

    Optional<VisitServiceItem> findByVisitIdAndServiceId(UUID visitId, UUID medicalServiceId);
}