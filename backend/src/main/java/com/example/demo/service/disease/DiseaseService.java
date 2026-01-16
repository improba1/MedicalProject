package com.example.demo.service.disease;

import com.example.demo.model.Disease;

import java.util.List;
import java.util.UUID;

public interface DiseaseService {
   List<Disease> search(String query);

    Disease getById(UUID id);
    Disease addDisease(Disease disease);
    Disease update(Disease disease);
    void deleteDisease(UUID id);
}