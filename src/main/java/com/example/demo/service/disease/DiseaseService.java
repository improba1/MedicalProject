package com.example.demo.service.disease;

import com.example.demo.dto.request.disease.UpdateDiseaseRequest;
import com.example.demo.dto.response.DiseaseResponse;
import com.example.demo.model.Disease;

import java.util.List;
import java.util.UUID;

public interface DiseaseService {
    List<Disease> searchByCode(String codePart);
    List<Disease> searchByName(String namePart);
    List<Disease> search(String query);

    Disease getById(UUID id);
    Disease addDisease(Disease disease);
    Disease update(Disease disease);
    void deleteDisease(UUID id);

    DiseaseResponse updateDisease(UUID id, UpdateDiseaseRequest request); // 🔹 новий метод
}