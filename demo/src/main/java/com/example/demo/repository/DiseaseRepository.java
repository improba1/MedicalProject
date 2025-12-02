package com.example.demo.repository;

import com.example.demo.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, UUID> {

    // 🔹 Пошук за назвою (частковий збіг)
    List<Disease> findByNameContainingIgnoreCase(String name);

    List<Disease> findByDiseaseCodeContainingIgnoreCase(String codePart);
}