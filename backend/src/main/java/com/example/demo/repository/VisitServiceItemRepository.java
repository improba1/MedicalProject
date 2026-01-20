package com.example.demo.repository;

import com.example.demo.model.VisitServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitServiceItemRepository extends JpaRepository<VisitServiceItem, UUID> {

    List<VisitServiceItem> findByVisitId(UUID visitId);

    void deleteByVisitId(UUID visitId);

    @Query("""
        select i
        from VisitServiceItem i
        join fetch i.service s
        join fetch i.visit v
        where v.id = :visitId
    """)
    List<VisitServiceItem> findByVisitIdWithRelations(UUID visitId);
}